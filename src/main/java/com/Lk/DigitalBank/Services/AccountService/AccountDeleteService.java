package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDeleteService {
    private final AccountRepository accountRepository;

    // DELETAR CONTA
    public void deleteAccount(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com Nº %s não existe.", accountNumber)));

        if (account.getAccountStatus() == AccountStatus.BLOCKED){
            throw new AccountInactiveException(String.format("ERRO! Conta com Nº %s não esta ativa.", accountNumber));
        }

        account.blockedAccount();
    }
}
