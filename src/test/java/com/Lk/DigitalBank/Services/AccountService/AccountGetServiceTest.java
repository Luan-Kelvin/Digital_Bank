package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountGetServiceTest {

    @Mock
    private Conversor conversor;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountGetService accountGetService;

    @Test
    public void deveRetornarContaQuandoIdExistir(){
        Long id = 1L;

        Account account = new Account();

        AccountGetDTO dto = new AccountGetDTO("12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "teste");

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        when(conversor.converterAccount(account)).thenReturn(dto);

        AccountGetDTO resultado = accountGetService.findById(id);

        assertEquals(dto, resultado);

        verify(accountRepository).findById(id);

        verify(conversor).converterAccount(account);
    }


    @Test
    public void deveLancarExceptionSeNaoExistir(){
        Long id = 99L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountGetService.findById(id));

        verify(accountRepository).findById(id);
    }


    @Test
    public void deveRetornarContaCOmNumeroSolicitado(){
        String number = "12345";

        Account account = new Account();

        AccountGetDTO dto = new AccountGetDTO("12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "teste");

        when(accountRepository.findByAccountNumber(number)).thenReturn(Optional.of(account));

        when(conversor.converterAccount(account)).thenReturn(dto);

        AccountGetDTO resultado = accountGetService.findByAccountNumber(number);

        assertEquals(dto, resultado);

        verify(accountRepository).findByAccountNumber(number);

        verify(conversor).converterAccount(account);
    }

    @Test
    public void lancarExcpetionSeCOntaNaoExistir(){
        String number = "54321";

        when(accountRepository.findByAccountNumber(number)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountGetService.findByAccountNumber(number));

        verify(accountRepository).findByAccountNumber(number);
    }
}
