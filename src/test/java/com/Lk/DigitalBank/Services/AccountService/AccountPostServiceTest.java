package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountPostServiceTest {

    @Mock
    private  Conversor conversor;

    @Mock
    private  NumberGenerator numberGenerator;

    @Mock
    private  AccountRepository accountRepository;

    @Mock
    private  CustomerRepository customerRepository;

    @InjectMocks
    private  AccountPostService accountPostService;

    @Test
    public void deveretornarUmaNovaContaCriada(){
        String cpf = "111.222.333-44";

        Account account = new Account();
        Customer customer = new Customer("Ronaldinho", cpf, LocalDate.of(2005, 12, 15));
        AccountGetDTO getDto = new AccountGetDTO("12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "Ronaldinho");
        AccountPostDTO postDto = new AccountPostDTO(cpf, AccountType.CURRENT);

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerAndAccountType(customer, AccountType.CURRENT)).thenReturn(false);
        when(numberGenerator.generateNumberAccount()).thenReturn("12345");
        when(conversor.converterAccount(any(Account.class))).thenReturn(getDto);

        AccountGetDTO accountGetDTO = accountPostService.createAccount(postDto);

        assertEquals(customer.getName(), accountGetDTO.customerName());

        verify(customerRepository).findByCpf(cpf);
        verify(accountRepository).existsByCustomerAndAccountType(customer, AccountType.CURRENT);
        verify(conversor).converterAccount(any(Account.class));
        verify(accountRepository).save(any(Account.class));
    }
}
