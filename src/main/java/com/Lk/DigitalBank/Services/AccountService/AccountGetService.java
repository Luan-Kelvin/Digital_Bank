package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.InvalidAccountStatusException;
import com.Lk.DigitalBank.Exception.InvalidAccountTypeException;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
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

    // BUSCAR CONTA POR ID
    public AccountGetDTO findById(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! nenhuma conta com ID %s encontrada.", id)));

        return conversor.converterAccount(account);
    }

    // BUSCAR POR NÚMERO DA CONTA
    public AccountGetDTO findByAccountNumber(String number){
        Account account = accountRepository.findByAccountNumber(number)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Account com número %s não existe.", number)));

        return conversor.converterAccount(account);
    }

    // BUSCAR POR STATUS DA CONTA
    public List<AccountGetDTO> searchByStatus(String status){
        AccountStatus st = null;

        if (status.equalsIgnoreCase("ACTIVE")){
            st = AccountStatus.ACTIVE;
        }else if (status.equalsIgnoreCase("BLOCKED")){
            st = AccountStatus.BLOCKED;
        } else if (status.equalsIgnoreCase("CLOSED")){
            st = AccountStatus.CLOSED;
        } else {
            throw new InvalidAccountStatusException(String.format("ERRO! não existe status %s ", status));
        }

        List<Account> accounts = accountRepository.findByAccountStatus(st);

        if (accounts.isEmpty()){
            System.out.println(String.format("Nenhuma Conta cadastrada com status %s", status.toUpperCase()));
            return List.of();
        }

        return accounts.stream().map(conversor::converterAccount).toList();
    }

    // BUSCRA POR TIPO
    public List<AccountGetDTO> searchByType(String type){
        AccountType ty = null;

        if (type.equalsIgnoreCase("CURRENT")){
            ty = AccountType.CURRENT;
        } else if (type.equalsIgnoreCase("SAVINGS")){
            ty = AccountType.SAVINGS;
        } else {
            throw new InvalidAccountTypeException(String.format("ERRO! não existe Type de conta %s", type));
        }

        List<Account> accounts = accountRepository.findByAccountType(ty);

        return accounts.stream().map(conversor::converterAccount).toList();
    }


    // BUSCAR CONTAS DE UM CLIENTE
    public List<AccountGetDTO> searchByCustomer(String cpf){
        if (!cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")){
            throw new InvalidCPFException("Erro! Formato de CPF Inválido.");
        }

        List<Account> accounts = accountRepository.findByCustomerCpf(cpf);

        if (accounts.isEmpty()){
            logger.info(String.format("Cliente com cpf %s... não possui nenhuma conta.", cpf.substring(0, 7)));
        }

        return accounts.stream().map(conversor::converterAccount).toList();
    }

}
