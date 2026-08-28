package com.Lk.DigitalBank.Services.AccountService;


import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPatchDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountPatchServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private Conversor conversor;

    @InjectMocks
    private AccounPatchService accounPatchService;

    @Test
    public void deveAlterarTypeDaConta(){
        Customer customer = new Customer();
        Account account = new Account(customer, AccountType.CURRENT);
        AccountGetDTO dto = new AccountGetDTO(1L, "12345", BigDecimal.valueOf(200.0), AccountType.SAVINGS, AccountStatus.ACTIVE, 1L, "Joaquim");
        AccountPatchDTO pathDto = new AccountPatchDTO("12345", AccountType.SAVINGS);
        account.addNumberAccount("12345");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(conversor.converterAccount(account)).thenReturn(dto);

        AccountGetDTO accountGetDTO = accounPatchService.updateType(pathDto);

        assertEquals(AccountType.SAVINGS,  accountGetDTO.accountType());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    public void deveMudarOStatusdDaContaParaBloqueado(){
        Account account = new Account();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accounPatchService.blockAccount(1L);

        assertEquals(AccountStatus.BLOCKED, account.getAccountStatus());

        verify(accountRepository).findById(1L);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void deveLancarExcecaoSeContaNaoExistir(){
        Long id = 1L;

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accounPatchService.blockAccount(id));

        verify(accountRepository).findById(id);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    public void deveAlncarExcecaoSeContaEstiverInativa(){
        Account account = new Account();
        account.blockedAccount();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(AccountInactiveException.class, () -> accounPatchService.blockAccount(1L));

        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any(Account.class));
    }

}
