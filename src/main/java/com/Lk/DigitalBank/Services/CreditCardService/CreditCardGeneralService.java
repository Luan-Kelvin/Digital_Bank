package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.ENUM.InvoiceStatus;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.CreditCardPurchase;
import com.Lk.DigitalBank.Entity.Invoice;
import com.Lk.DigitalBank.Entity.PurchaseInstallment;
import com.Lk.DigitalBank.Exception.CardIsNotActiveForPurchasseException;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Exception.InvalidLimitValueException;
import com.Lk.DigitalBank.Exception.InvalidPasswordException;
import com.Lk.DigitalBank.Repository.CreditCardPurchseRespository;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import com.Lk.DigitalBank.Repository.PurchaseInstallmentRepository;
import com.Lk.DigitalBank.Services.InvoiceService.InvoiceService;
import jakarta.persistence.Entity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class CreditCardGeneralService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardGetService.class);
    private final CreditCardRepository creditCardRepository;
    private final PurchaseInstallmentRepository purchaseInstallmentRepository;
    private final CreditCardPurchseRespository creditCardPurchseRespository;
    private final InvoiceService invoiceService;


    // FAZER UMA COMPRA
    @Transactional
    public void makePurchase(String cardNumber, String password,  BigDecimal value, String merchant, Integer installments){
        CreditCard creditCard = creditCardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new CreditCardsNotExistException(String.format("ERRO! cartão com número %s não existe", cardNumber)));

        //verificar status
        if (creditCard.getCardStatus() != CardStatus.ACTIVE){
            throw new CardIsNotActiveForPurchasseException("ERRO! Cartão nao esta ativo para compras.");
        }

        // verificar senha
        if (!creditCard.checkCompatibility(password)){
            throw new InvalidPasswordException("ERRO! Senha incorreta.");
        }

        // verificar limite
        if (!creditCard.hasLimit(value)){
            throw new InvalidLimitValueException("ERRO! não ha limite suficiente para compra.");
        }

        String description = String.format("Compra realizada em %s no valor de R$%s parcelada em %s vezes", merchant, value, installments);

        CreditCardPurchase creditCardPurchase = new CreditCardPurchase(description, value, installments, merchant, creditCard);

        // consumir limite
        creditCard.purchase(value);

        // descobrir a primeira fatura
        YearMonth firstMonthInvoice = calculateFirsInvoiceMonth(creditCard, creditCardPurchase.getPurchaseDate().toLocalDate());

        // Calcular valor da parcelas
        BigDecimal valueInntallments = value.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);


        for (int i = 1;i <= installments;i++){
            YearMonth invoiceMonth = firstMonthInvoice.plusMonths(i - 1);

            Invoice invoice = invoiceService.findOrCreateInvoice(creditCard, invoiceMonth);

            PurchaseInstallment purchaseInstallment = new PurchaseInstallment(
                    i,
                    installments,
                    valueInntallments,
                    InvoiceStatus.OPEN,
                    creditCardPurchase,
                    invoice
            );

            invoice.addpurchaseInstallment(purchaseInstallment);

            purchaseInstallmentRepository.save(purchaseInstallment);
            creditCardPurchase.addInstallments(purchaseInstallment);

        }
        creditCardPurchseRespository.save(creditCardPurchase);
        creditCard.addCreditCardPurchase(creditCardPurchase);
        creditCardRepository.save(creditCard);
    }

    // DESCOBRIR PRIMEIRA FATURA
    private YearMonth calculateFirsInvoiceMonth(CreditCard creditCard, LocalDate purchasedate){
        if (purchasedate.getDayOfMonth() <= creditCard.getClosingDayInvoice()){
            return YearMonth.from(purchasedate);
        }

        return  YearMonth.from(purchasedate).plusMonths(1);
    }

}
