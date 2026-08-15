package com.Lk.DigitalBank.Services.CreditCardPurchaseService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCradPurchase.CreditCardPurchaseGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCradPurchase.CreditCardPurchasePostDTO;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.CreditCardPurchase;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Repository.CreditCardPurchseRespository;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreditCardPurchasePostService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardPurchaseGetService.class);
    private final CreditCardPurchseRespository creditCardPurchseRespository;
    private final CreditCardRepository creditCardRepository;
    private final Conversor conversor;

    // CRIAR
    public CreditCardPurchaseGetDTO createCreditCardPurchase(CreditCardPurchasePostDTO dto){
        CreditCard creditCard = creditCardRepository.findById(dto.creditCardId())
                .orElseThrow(() -> new CreditCardsNotExistException("Erro! crtão de crédito não foi encontrado."));

        CreditCardPurchase cp = new CreditCardPurchase(
                dto.description(),
                dto.amountPurchase(),
                dto.installments(),
                dto.merchant(),
                creditCard
                );

        creditCard.recordNewPurchase(cp);

        creditCardPurchseRespository.save(cp);

        return conversor.converterCreditCardPurchase(cp);

    }
}
