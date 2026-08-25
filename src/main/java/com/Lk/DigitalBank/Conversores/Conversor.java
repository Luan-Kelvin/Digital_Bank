package com.Lk.DigitalBank.Conversores;

import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCradPurchase.CreditCardPurchaseGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.DTOs.Invoice.InvoiceGetDTO;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionGetDTO;
import com.Lk.DigitalBank.Entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Conversor {

    // CONVERTER ACCOUNT PARA ACCOUNTGETDTO
    public AccountGetDTO converterAccount(Account account){
        return new AccountGetDTO(
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getAccountStatus(),
                account.getCustomer().getId(),
                account.getCustomer().getName()
        );
    }

    // CONVERTER CUSTOMER PARA CUSTOMERGETDTO
    public CustomerGetDTO converterCustomer(Customer customer){
        List<String> accounts = customer.getAccounts().stream().map(Account::getAccountNumber).toList();

        return new CustomerGetDTO(
                customer.getId(),
                customer.getName(),
                customer.getDateOfBirth(),
                accounts
        );
    }

    // CONVERTER CREDITCARD PARA CREDITCARDGETDTO
    public CreditCardGetDTO converterCreditCard(CreditCard creditCard){
        return new CreditCardGetDTO(
                creditCard.getId(),
                creditCard.getExpirationDate(),
                creditCard.getCreditLimit(),
                creditCard.getAccount().getId()
        );
    }

    // CONVERTER CREDITCARDPURCHASE PARA CREDITCARDPURCHASEGETDTO
    public CreditCardPurchaseGetDTO converterCreditCardPurchase(CreditCardPurchase purchase){
        return new CreditCardPurchaseGetDTO(
                purchase.getId(),
                purchase.getDescription(),
                purchase.getAmountPurchase(),
                purchase.getPurchaseDate(),
                purchase.getInstallments(),
                purchase.getInstallmentAmount(),
                purchase.getMerchant(),
                purchase.getCreditCard().getId()
        );
    }

    // COVERTER INVOICE EM INVOICEGETDTO
    public InvoiceGetDTO converterInvoice(Invoice i){
        return new InvoiceGetDTO(
                i.getId(),
                i.getReferenceMonth(),
                i.getClosingDate(),
                i.getDueDate(),
                i.getTotalAmount(),
                i.getCreditCard().getId()
        );
    }

    // CONVERTER TRANSACTION EM TRANSACTIONGETDTO
    public TransactionGetDTO converterTransaction(Transaction t){

        return new TransactionGetDTO(
                t.getId(),
                t.getTransactionType(),
                t.getTransactionValue(),
                t.getDateAndTime(),
                t.getDescription(),
                t.getAccount().getAccountNumber()
        );
    }
}
