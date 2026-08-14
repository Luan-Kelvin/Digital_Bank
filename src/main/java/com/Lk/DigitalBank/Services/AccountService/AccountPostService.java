package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.AccountAlreadyExistsException;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountPostService {
    private final Logger logger = LoggerFactory.getLogger(AccountGetService.class);
    private final Conversor conversor;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final NumberGenerator numberGenerator;

    // CRIAR CONTA
    public AccountGetDTO createAccount(AccountPostDTO accountPostDTO){
        Customer customer = customerRepository.findByCpf(accountPostDTO.cpfCustomer())
                .orElseThrow(() -> new CustomerDoesNotExistException("ERRO! Cliente não existe no banco."));

        if (accountRepository.existsByCustomerAndAccountType(customer, accountPostDTO.accountType())){
            throw new AccountAlreadyExistsException("ERRO! Conta ja existente.");
        }

        Account account = new Account(customer, accountPostDTO.accountType());
        account.addNumberAccount(numberGenerator.generateNumberAccount());

        customer.addAccount(account);

        accountRepository.save(account);
        logger.info("Conta "+account.getAccountNumber()+" criada com sucesso!");

        return conversor.converterAccount(account);
    }
}
