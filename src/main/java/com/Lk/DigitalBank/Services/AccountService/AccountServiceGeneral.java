package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.ENUM.TransactionType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Transaction;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountServiceGeneral {
    private final Logger logger = LoggerFactory.getLogger(AccountGetService.class);
    private final Conversor conversor;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final NumberGenerator numberGenerator;

    // DEPOSITAR DINHEIRO
    @Transactional
    public void deposit(String numberAccount, BigDecimal value){
        Account account = accountRepository.findByAccountNumber(numberAccount)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com número %s não existe.", numberAccount)));

        account.deposit(value);

        String descriprion = String.format("Déposito feito no valor de R$%s", value);
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, value, descriprion, account);

        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        logger.info(String.format("Déposito no valor de R$%s realizado com sucesso!", value));
    }

    // SACAR DINHEIRO
    @Transactional
    public void withdraw(String numberAccount, BigDecimal value){
        Account account = accountRepository.findByAccountNumber(numberAccount)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com número %s não existe.", numberAccount)));

        account.withdraw(value);

        String description = String.format("Saque feito no valor de R$%s", value);
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, value, description, account);

        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        logger.info(String.format("Saque no valor de R$%s realizado com sucesso!", value));
    }

}
