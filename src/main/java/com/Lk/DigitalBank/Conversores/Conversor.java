package com.Lk.DigitalBank.Conversores;

import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return new CustomerGetDTO(
                customer.getId(),
                customer.getName(),
                customer.getDateOfBirth(),
                customer.getAccounts()
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
}
