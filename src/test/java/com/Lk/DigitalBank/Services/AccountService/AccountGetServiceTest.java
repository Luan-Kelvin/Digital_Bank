package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountBalanceDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountGetServiceTest {

    @Mock
    private Conversor conversor;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountGetService accountGetService;

    @Test
    @DisplayName("Deve retornar conta de acordo com o id passado.")
    public void retornarContaDoId(){
        Long id = 1L;

        Account account = new Account();

        AccountGetDTO dto = new AccountGetDTO(1L, "12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "teste");

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        when(conversor.converterAccount(account)).thenReturn(dto);

        AccountGetDTO resultado = accountGetService.findById(id);

        assertEquals(dto, resultado);

        verify(accountRepository).findById(id);

        verify(conversor).converterAccount(account);
    }


    @Test
    @DisplayName("Deve lançar exceção se nao existir conta com o id solicitado.")
    public void lancarExcecaoSeNaoExistir(){
        Long id = 99L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountGetService.findById(id));

        verify(accountRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar o saldo da conta")
    public void retornarSaldo(){
        Account account = new Account();
        account.addNumberAccount("12345");
        account.deposit(BigDecimal.valueOf(200));

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        AccountBalanceDTO dto = accountGetService.checkBalance(account.getAccountNumber());

        assertEquals(dto.balance(), account.getBalance());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());

    }

    @Test
    @DisplayName("Deve lançar exceção se conta conta não existir")
    public void lancaExcecaoSeContaNaoExiste(){
        Account account = new Account();
        account.addNumberAccount("12345");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountGetService.checkBalance("12345"));

        verify(accountRepository).findByAccountNumber("12345");
    }

    @Test
    @DisplayName("Deve lançar exceção se conta nao estiver ativa na hora da consulta de saldo.")
    public void lancaExcecaoParaContaInativa(){
        Account account = new Account();
        account.addNumberAccount("12345");
        account.blockedAccount();

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        assertThrows(AccountInactiveException.class, () -> accountGetService.checkBalance("12345"));

        verify(accountRepository).findByAccountNumber("12345");
    }


    @Test
    @DisplayName("Deve retornar a conta que tem o número solicitado")
    public void retornarContaQueTemNumerSolicitado(){
        String number = "12345";

        Account account = new Account();

        AccountGetDTO dto = new AccountGetDTO(1L, "12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "teste");

        when(accountRepository.findByAccountNumber(number)).thenReturn(Optional.of(account));

        when(conversor.converterAccount(account)).thenReturn(dto);

        AccountGetDTO resultado = accountGetService.findByAccountNumber(number);

        assertEquals(dto, resultado);

        verify(accountRepository).findByAccountNumber(number);

        verify(conversor).converterAccount(account);
    }

    @Test
    @DisplayName("Lançar exceção se conta não existir")
    public void lancaExcecaoSeNaoContaNoaExisteNoBanco(){
        String number = "54321";

        when(accountRepository.findByAccountNumber(number)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountGetService.findByAccountNumber(number));

        verify(accountRepository).findByAccountNumber(number);
    }


    @Test
    @DisplayName("Deve realizar busca por cpf do cliente")
    public void buscarClienteDOCPF(){
        Account account = new Account();

        AccountGetDTO dto1 = new AccountGetDTO(1L, "12345", BigDecimal.ZERO, AccountType.CURRENT, AccountStatus.ACTIVE, 1L, "teste");

        String cpf = "123.456.789-22";

        List<Account> accounts = List.of(account);

        when(accountRepository.findByCustomerCpf(cpf)).thenReturn(accounts);

        when(conversor.converterAccount(account)).thenReturn(dto1);

        List<AccountGetDTO> dtos = accountGetService.searchByCustomer(cpf);

        assertEquals(1, dtos.size());

        verify(accountRepository).findByCustomerCpf(cpf);
        verify(conversor).converterAccount(account);

    }

    @Test
    @DisplayName("Deve lancar Exceção se cpf for inválido.")
    public void lancaExcecaoSeCPFForInvalidoo(){
        String cpf = "12345678922";

        assertThrows(InvalidCPFException.class, () -> accountGetService.searchByCustomer(cpf));;

        verify(accountRepository, never()).findByCustomerCpf(anyString());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se o cliente nao possui contas.")
    public void retrnarListaVaziaParaClientesSemCOntas(){
        String cpf = "123.456.789-22";

        when(accountRepository.findByCustomerCpf(cpf)).thenReturn(List.of());

        List<AccountGetDTO> dtos = accountGetService.searchByCustomer(cpf);

        assertTrue(dtos.isEmpty());

        verify(accountRepository).findByCustomerCpf(cpf);

        verifyNoInteractions(conversor);
    }
}
