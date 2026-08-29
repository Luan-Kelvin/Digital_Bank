package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPatchDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountPatchService {
    private final Logger logger = LoggerFactory.getLogger(AccountPatchService.class);
    private final AccountRepository accountRepository;
    private final Conversor conversor;

    // ALTERAR TYPE
    public AccountGetDTO updateType(AccountPatchDTO dto){
        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com Nº %s não existe.", dto.accountNumber())));

        String oldName = account.getAccountType().name();
        account.setAccountType(dto.type());
        logger.info(String.format("Type de conta atualizado com sucesso! ANTIGO: %s | NOVO: %s", oldName, account.getAccountType().name()));
        accountRepository.save(account);

        return conversor.converterAccount(account);
    }

    // BLOQUEAR CONTA
    public void blockAccount(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com ID = %s não existe.", id)));

        if (!account.isActive()){
            throw new AccountInactiveException("ERRO! Conta esta inátiva");
        }

        account.blockedAccount();
        accountRepository.save(account);
        logger.info(String.format("Conta com ID = %s esta bloqueada.", id));
    }
}
