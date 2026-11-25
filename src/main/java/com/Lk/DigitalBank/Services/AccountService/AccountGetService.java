package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountBalanceDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountStatementDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.*;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Services.AccountEvents.AccountBalanceReconstructor;
import com.Lk.DigitalBank.Services.AccountEvents.AccountStatementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountGetService {
    private final Logger logger = LoggerFactory.getLogger(AccountGetService.class);
    private final Conversor conversor;
    private final AccountRepository accountRepository;
    private final AccountBalanceReconstructor accountBalanceReconstructor;
    private final AccountStatementService accountStatementService;


    // LISTAR CONTAS ATIVAS NO BANCO
    @Transactional(readOnly = true)
    public List<AccountGetDTO> listAccountsAcitives(){
        List<Account> accounts = accountRepository.searchActives();

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

    // LISTAR CONTAS INATIVAS NO BANCO
    @Transactional(readOnly = true)
    public List<AccountGetDTO> listAccountsInactive(){
        List<Account> accounts = accountRepository.searchInactives();

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

    // BUSCAR CONTA POR ID (CONTROLLER)
    @Transactional(readOnly = true)
    public AccountGetDTO findByIdController(Long id){
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! nenhuma conta com ID %s encontrada.", id)));

        return conversor.converterAccount(account);
    }

    // BUSCAR CONTA POR ID (CLASSES)

    public Account findById(Long id){
        return accountRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! nenhuma conta com ID %s encontrada.", id)));
    }

    // BUSCAR POR NÚMERO DA CONTA
    @Transactional(readOnly = true)
    public AccountGetDTO findByAccountNumber(String number){
        Account account = accountRepository.findByAccountNumber(number)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Account com número %s não existe.", number)));

        return conversor.converterAccount(account);
    }

    // CONSULTAR SALDO
    @Transactional(readOnly = true)
    public AccountBalanceDTO checkBalance(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException(String.format("ERRO! Conta com Nº %s não existe.", accountNumber)));

        if (!account.isActive()){
            throw new AccountInactiveException(String.format("ERRO! Conta Nº %s está inativa.", accountNumber));
        }

        return new AccountBalanceDTO(accountBalanceReconstructor.reconstructBalance(account.getId()));
    }

    // BUSCAR POR STATUS DA CONTA
    @Transactional(readOnly = true)
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
            logger.info(String.format("Nenhuma Conta cadastrada com status %s", status.toUpperCase()));
            return List.of();
        }

        return accounts.stream().map(conversor::converterAccount).toList();
    }

    // BUSCAR POR TIPO
    @Transactional(readOnly = true)
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

        if (accounts.isEmpty()){
            logger.info(String.format("ERRO!não foi encontrado nenhum conta do tipo %s.", ty));
            return List.of();
        }

        return accounts.stream().map(conversor::converterAccount).toList();
    }


    // BUSCAR CONTAS DE UM CLIENTE
    @Transactional(readOnly = true)
    public List<AccountGetDTO> searchByCustomer(String cpf){
        if (!cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")){
            throw new InvalidCPFException("Erro! Formato de CPF Inválido.");
        }

        List<Account> accounts = accountRepository.findByCustomerCpf(cpf);

        if (accounts.isEmpty()){
            logger.info(String.format("Cliente com cpf %s... não possui nenhuma conta.", cpf.substring(0, 7)));
            return List.of();
        }

        return accounts.stream().map(conversor::converterAccount).toList();
    }

    // GERAR RELATÓRIO
    @Transactional(readOnly = true)
    public AccountStatementDTO generateStatement(String accountNumber){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountDoesNotExistException("ERRO! Conta não foi enontrada."));

        if (!account.isActive()){
            throw new AccountInactiveException(String.format("ERRO! Conta Nº %s está inativa.", accountNumber));
        }

        return accountStatementService.generateStatement(account.getId());

    }

}