package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPostDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Exception.AccountAlreadyHasCreditCardException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredtiCardPostService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardGetService.class);
    private final Conversor conversor;
    private final NumberGenerator numberGenerator;
    private final CreditCardRepository creditCardRepository;
    private final AccountRepository accountRepository;

    // CRIAR CARTÃO DE CRÉDITO
    public CreditCardGetDTO createCreditCard(CreditCardPostDTO creditCardPostDTO){
        Optional<Account> account = accountRepository.findById(creditCardPostDTO.idAccount());

        if (account.isPresent()){
            throw new AccountAlreadyHasCreditCardException(String.format("ERRO! Conta com ID = %s ja possui cartão de crédito.", creditCardPostDTO.idAccount()));
        }
        String cardNumber = numberGenerator.generateNumberCard();

        CreditCard creditCard = new CreditCard(
                creditCardPostDTO.password(),
                cardNumber, account.get(),
                creditCardPostDTO.closingDayInvoice());

        return conversor.converterCreditCard(creditCard);
    }
}
