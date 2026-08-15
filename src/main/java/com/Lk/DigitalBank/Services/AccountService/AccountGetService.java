package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountGetService {
    private final Logger logger = LoggerFactory.getLogger(AccountGetService.class);
    private final Conversor conversor;
    private final AccountRepository accountRepository;


    // LISTAR CONTAS EXISTENTE NO BANCO
    public List<AccountGetDTO> listAccounts(){
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()){
            logger.info("Nenhuma conta encontrada.");
            return List.of();
        }

        List<AccountGetDTO> dtos = new ArrayList<>();

        accounts.forEach(a -> {
            dtos.add(conversor.converterAccount(a));
        });

        return dtos;
    }


}
