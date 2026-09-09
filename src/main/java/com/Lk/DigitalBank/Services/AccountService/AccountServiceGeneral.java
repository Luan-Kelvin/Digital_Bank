package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.DepositAndWithDrawAccountDTO;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionGetDTO;
import com.Lk.DigitalBank.ENUM.TransactionType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Transaction;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Repository.AccountRepository;
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
    private final Logger logger = LoggerFactory.getLogger(AccountServiceGeneral.class);
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Conversor conversor;

    // DEPOSITAR DINHEIRO
    @Transactional
    public TransactionGetDTO deposit(DepositAndWithDrawAccountDTO dto){
        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com número %s não existe.", dto.accountNumber())));

        if (!account.isActive()){
            throw new AccountInactiveException("ERRO! Status de conta esta inátivo.");
        }

        account.deposit(dto.value());

        String descriprion = String.format("Déposito feito no valor de R$%s", dto.value());
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, dto.value(), descriprion);

        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        logger.info(String.format("Déposito no valor de R$%s realizado com sucesso!", dto.value()));

        return conversor.converterTransaction(transaction);
    }

    // SACAR DINHEIRO
    @Transactional
    public TransactionGetDTO withdraw(DepositAndWithDrawAccountDTO dto){
        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com número %s não existe.", dto.accountNumber())));

        if (!account.isActive()){
            throw new AccountInactiveException("ERRO! Status de conta esta inátivo.");
        }

        account.withdraw(dto.value());

        String description = String.format("Saque feito no valor de R$%s", dto.value());
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, dto.value(), description);

        account.addTransaction(transaction);
        transactionRepository.save(transaction);

        logger.info(String.format("Saque no valor de R$%s realizado com sucesso!", dto.value()));

        return conversor.converterTransaction(transaction);
    }



}
