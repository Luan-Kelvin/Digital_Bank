package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AccountDeleteServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountDeleteService accountDeleteService;

    @Test
    public void deveMudarOStatusDaContaParaBlocked(){
        Account account = new Account();
        account.addNumberAccount("12345");

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        accountDeleteService.deleteAccount(account.getAccountNumber());

        assertEquals(AccountStatus.BLOCKED, account.getAccountStatus());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void deveLancarExcecaoSeAccountnaoExistir(){

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountDeleteService.deleteAccount("12345"));

        verify(accountRepository).findByAccountNumber("12345");
        verify(accountRepository, never()).save(any(Account.class));
    }
}
