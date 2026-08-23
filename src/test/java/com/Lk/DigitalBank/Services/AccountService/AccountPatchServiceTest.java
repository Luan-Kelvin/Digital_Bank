package com.Lk.DigitalBank.Services.AccountService;


import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountPatchServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccounPatchService accounPatchService;

    @Test
    public void deveAlterarTypeDaConta(){
        Customer customer = new Customer();
        Account account = new Account(customer, AccountType.CURRENT);
        account.addNumberAccount("12345");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));

        accounPatchService.updateType("savings", account.getAccountNumber());

        assertEquals(AccountType.SAVINGS,  account.getAccountType());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

}
