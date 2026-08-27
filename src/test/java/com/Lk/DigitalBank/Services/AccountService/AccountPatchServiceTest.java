package com.Lk.DigitalBank.Services.AccountService;


import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPatchDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        AccountGetDTO dto = new AccountGetDTO("12345", BigDecimal.valueOf(200.0), AccountType.SAVINGS, AccountStatus.ACTIVE, 1L, "Joaquim");
        AccountPatchDTO pathDto = new AccountPatchDTO("12345", AccountType.SAVINGS);
        account.addNumberAccount("12345");

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(conversor.converterAccount(account)).thenReturn(dto);

        AccountGetDTO accountGetDTO = accounPatchService.updateType(pathDto);

        assertEquals(AccountType.SAVINGS,  accountGetDTO.accountType());

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
    }

}
