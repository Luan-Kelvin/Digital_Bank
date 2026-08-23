package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.InvalidAccountTypeException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccounPatchService {
    private final Logger logger = LoggerFactory.getLogger(AccounPatchService.class);
    private final AccountRepository accountRepository;

    // ALTERAR TYPE
    public void updateType(String type, String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com Nº %s não existe.", accountNumber)));

        AccountType newType = null;

        if (type.equalsIgnoreCase("current")){
            newType = AccountType.CURRENT;
        } else if (type.equalsIgnoreCase("savings")) {
            newType = AccountType.SAVINGS;
        }else {
            throw new InvalidAccountTypeException(String.format("ERRO! Type %s é inválido.", type));
        }

        if (newType != account.getAccountType()){
            account.setAccountType(newType);
        }
    }
}
