package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.AccountAlreadyExistsException;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        AccountGetDTO getDto = new AccountGetDTO(1L, "12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "Ronaldinho");
        AccountPostDTO postDto = new AccountPostDTO(cpf, AccountType.CURRENT);

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerAndAccountType(customer, AccountType.CURRENT)).thenReturn(false);
        when(numberGenerator.generateNumberAccount()).thenReturn("12345");
        when(conversor.converterAccount(any(Account.class))).thenReturn(getDto);

        AccountGetDTO accountGetDTO = accountPostService.createAccount(postDto);

        assertEquals(customer.getName(), accountGetDTO.customerName());
        assertEquals("12345", getDto.accountNumber());
        assertEquals(BigDecimal.ZERO, getDto.balance());
        assertEquals(AccountType.CURRENT, getDto.accountType());
        assertEquals(AccountStatus.ACTIVE, getDto.accountStatus());


        verify(customerRepository).findByCpf(cpf);
        verify(accountRepository).existsByCustomerAndAccountType(customer, AccountType.CURRENT);
        verify(numberGenerator).generateNumberAccount();
        verify(accountRepository).save(any(Account.class));
        verify(conversor).converterAccount(any(Account.class));
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void deveretornarExceptionSeCustomernaoExistir(){
        String cpf = "225.456.787-54";
        AccountPostDTO postDto = new AccountPostDTO(cpf, AccountType.CURRENT);

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(CustomerDoesNotExistException.class, () -> accountPostService.createAccount(postDto));

        verify(customerRepository).findByCpf(cpf);
        verify(accountRepository, never()).save(any(Account.class));
        verify(numberGenerator, never()).generateNumberAccount();
    }

    @Test
    public void deveLancarExcecaoSeJaExistirUmaContaComMesmoTypeEMesmoCliente(){
        String cpf = "544.787.478-26";
        Customer customer = new Customer("Laun Rocha", cpf, LocalDate.of(2002, 12, 21));

        AccountPostDTO postDto = new AccountPostDTO(cpf, AccountType.CURRENT);

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));
        when(accountRepository.existsByCustomerAndAccountType(customer, AccountType.CURRENT)).thenReturn(true);

        assertThrows(AccountAlreadyExistsException.class, () -> accountPostService.createAccount(postDto));

        verify(customerRepository).findByCpf(cpf);
        verify(accountRepository).existsByCustomerAndAccountType(customer, AccountType.CURRENT);
        verify(numberGenerator, never()).generateNumberAccount();
        verify(accountRepository, never()).save(any(Account.class));
    }
}
