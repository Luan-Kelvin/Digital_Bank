package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionGetDTO;
import com.Lk.DigitalBank.ENUM.TransactionType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Transaction;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountSerivcegeneralTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Conversor conversor;

    @InjectMocks
    private AccountServiceGeneral accountServiceGeneral;

    @Test
    public void deveRetornarTransactionQuandoDepositoForFeito(){
        Account account = new Account();
        account.addNumberAccount("12345");

        Transaction transaction = new Transaction();
        TransactionGetDTO dto = new TransactionGetDTO(
                1L,
                TransactionType.DEPOSIT,
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                "Depósito em dinheiro",
                "12345"
                );

        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account));
        when(conversor.converterTransaction(transaction)).thenReturn(dto);

        TransactionGetDTO transactionGetDTO = accountServiceGeneral.deposit("12345", BigDecimal.valueOf(100));

        assertEquals("12345", transactionGetDTO.accountNumber());
        assertEquals(1L, transactionGetDTO.id());
        assertEquals(BigDecimal.valueOf(100), transactionGetDTO.value());
        assertEquals("Depósito em dinheiro", transactionGetDTO.description());

        verify(accountRepository).findByAccountNumber("12345");
        verify(conversor).converterTransaction(transaction);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void deveLancarExcecaoSeContaNaoExistir(){
        String numberAccount = "12345";

        when(accountRepository.findByAccountNumber(numberAccount)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountServiceGeneral.deposit(numberAccount, BigDecimal.valueOf(200)));

        verify(accountRepository).findByAccountNumber(numberAccount);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(conversor, never()).converterTransaction(any(Transaction.class));
    }

    @Test
    public void develancarExcecaoSeContaEstiverDesativadaParaDeposito(){
        String numberAccount = "12345";
        Account account = new Account();
        account.blockedAccount();
        account.addNumberAccount(numberAccount);

        when(accountRepository.findByAccountNumber(numberAccount)).thenReturn(Optional.of(account));

        assertThrows(AccountInactiveException.class, () -> accountServiceGeneral.deposit(numberAccount, BigDecimal.valueOf(100)));

        verify(accountRepository).findByAccountNumber(numberAccount);
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(conversor, never()).converterTransaction(any(Transaction.class));

    }

    @Test
    public void deveRetornarTransactionQuandoSaqueForFeito(){
        String accoutnNumber = "12345";
        Account account = new Account();
        account.deposit(BigDecimal.valueOf(200));
        account.addNumberAccount(accoutnNumber);

        Transaction transaction = new Transaction();
        TransactionGetDTO dto = new TransactionGetDTO(
                1L,
                TransactionType.WITHDRAW,
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                "Saque em dinheiro",
                "12345"
        );

        when(accountRepository.findByAccountNumber(accoutnNumber)).thenReturn(Optional.of(account));
        when(conversor.converterTransaction(transaction)).thenReturn(dto);

        TransactionGetDTO transactionGetDTO = accountServiceGeneral.withdraw(accoutnNumber, BigDecimal.valueOf(100));

        assertEquals(1L, transactionGetDTO.id());
        assertEquals("12345", transactionGetDTO.accountNumber());
        assertEquals(TransactionType.WITHDRAW, transactionGetDTO.type());
        assertEquals(BigDecimal.valueOf(100), transactionGetDTO.value());
        assertEquals("Saque em dinheiro", transactionGetDTO.description());

        verify(accountRepository).findByAccountNumber(accoutnNumber);
        verify(conversor).converterTransaction(transaction);
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void deveLancarExcecaoSeContaNaoExistirParaSaque(){
        String accounNumber = "12345";

        Account account = new Account();
        account.addNumberAccount(accounNumber);
        account.deposit(BigDecimal.valueOf(200));

        Transaction transaction = new Transaction();

        when(accountRepository.findByAccountNumber(accounNumber)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountServiceGeneral.withdraw(accounNumber, BigDecimal.valueOf(100)));

        verify(accountRepository).findByAccountNumber(accounNumber);
        verify(conversor, never()).converterTransaction(transaction);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void deveLancarExcecaoSeContaEstiverDesativadaParaSaque(){
        Account account = new Account();
        account.addNumberAccount("12345");
        account.blockedAccount();
        account.deposit(BigDecimal.valueOf(200));

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(AccountInactiveException.class, () -> accountServiceGeneral.withdraw(account.getAccountNumber(), BigDecimal.valueOf(100)));

        verify(accountRepository).findByAccountNumber(account.getAccountNumber());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(conversor, never()).converterTransaction(any(Transaction.class));
    }
}
