package com.Lk.DigitalBank.Services.AccountService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.DTOs.Account.TransferPixDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.ENUM.TransactionType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Entity.Transaction;
import com.Lk.DigitalBank.Exception.*;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;

import com.Lk.DigitalBank.Repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private  AccountPostService accountPostService;

    @Test
    @DisplayName("Deve retornar uma nova conta criada.")
    public void criarNovaConta(){
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

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        verify(accountRepository).save(captor.capture());

        Account accountSave = captor.getValue();

        assertEquals(postDto.cpfCustomer(), accountSave.getCustomer().getCpf());
        assertEquals(postDto.accountType(), accountSave.getAccountType());

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
    @DisplayName("Deve retornar exception se cliente não existir")
    public void lancaExceptionSeClienteNaoForEncontrado(){
        String cpf = "225.456.787-54";
        AccountPostDTO postDto = new AccountPostDTO(cpf, AccountType.CURRENT);

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(CustomerDoesNotExistException.class, () -> accountPostService.createAccount(postDto));

        verify(customerRepository).findByCpf(cpf);
        verify(accountRepository, never()).save(any(Account.class));
        verify(numberGenerator, never()).generateNumberAccount();
    }

    @Test
    @DisplayName("Deve lançar exceção se ja existir conta com mesmo type e mesmo cliente")
    public void deveLancarExcecaoSeContaJaForCriadas(){
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

    @Test
    @DisplayName("Deve realizar uma transferêcia pix entre duas contas e salvar essas transferência e retornar um dto")
    void deveRealizarTransferenciaPixComSuceso(){
        Customer customer1 = new Customer("Ronaldinho", "123.456.789-10", LocalDate.of(2005, 12, 15));
        Customer customer2 = new Customer("Romario", "222.333.444-19", LocalDate.of(2005, 12, 15));


        Account recipient = new Account(customer1, AccountType.CURRENT);
        recipient.addNumberAccount("2020 2020 2020 2020");

        Account sender = new Account(customer2, AccountType.CURRENT);
        sender.addNumberAccount("1010 1010 1010 1010");

        sender.deposit(BigDecimal.valueOf(500));

        TransferPixDTO dtoPix = new TransferPixDTO(
                "1010 1010 1010 1010",
                "2020 2020 2020 2020",
                BigDecimal.valueOf(200)
        );

        when(accountRepository.findByAccountNumber("2020 2020 2020 2020")).thenReturn(Optional.of(recipient));
        when(accountRepository.findByAccountNumber("1010 1010 1010 1010")).thenReturn(Optional.of(sender));

        accountPostService.transferViaPix(dtoPix);

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        verify(accountRepository, times(2)).save(accountCaptor.capture());

        List<Account> accountList = accountCaptor.getAllValues();

        Account accountSender = accountList.get(0);
        Account accountRecipient = accountList.get(1);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository, times(2)).save(transactionCaptor.capture());

        List<Transaction> transactionList = transactionCaptor.getAllValues();

        Transaction transactionSender = transactionList.get(0);
        Transaction transactionRecipient = transactionList.get(1);

        assertEquals(transactionSender.getTransactionValue(), dtoPix.value());
        assertEquals(transactionSender.getAccount().getAccountNumber(), dtoPix.senderAccount());
        assertEquals( TransactionType.PIX_SENT, transactionSender.getTransactionType());

        assertEquals(transactionRecipient.getTransactionValue(), dtoPix.value());
        assertEquals(transactionRecipient.getAccount().getAccountNumber(), dtoPix.recipientAccount());
        assertEquals( TransactionType.PIX_RECEVIED, transactionRecipient.getTransactionType());

        assertEquals(BigDecimal.valueOf(300), accountSender.getBalance());
        assertEquals(dtoPix.senderAccount(), accountSender.getAccountNumber());

        assertEquals(BigDecimal.valueOf(200), accountRecipient.getBalance());
        assertEquals(dtoPix.recipientAccount(), accountRecipient.getAccountNumber());

        verify(accountRepository).findByAccountNumber("2020 2020 2020 2020");
        verify(accountRepository).findByAccountNumber("1010 1010 1010 1010");
    }


    @Test
    @DisplayName("Deve lançar exceção se conta não existir")
    void deveLancarExcecaoSeContaNaoExistir(){
        TransferPixDTO dtoPix = new TransferPixDTO(
                "1010 1010 1010 1010",
                "2020 2020 2020 2020",
                BigDecimal.valueOf(200)
        );

        when(accountRepository.findByAccountNumber("2020 2020 2020 2020")).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> accountPostService.transferViaPix(dtoPix));

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Lançar exceção se conta do cliente que recebe o pix seja inativa")
    void deveLancarExcecaoSeContaDoRecipientEstejaInativa(){
        Account recipient = new Account();
        recipient.addNumberAccount("2020 2020 2020 2020");
        recipient.blockedAccount();

        Account sender = new Account();
        sender.addNumberAccount("1010 1010 1010 1010");

        TransferPixDTO dtoPix = new TransferPixDTO(
                "1010 1010 1010 1010",
                "2020 2020 2020 2020",
                BigDecimal.valueOf(200)
        );

        when(accountRepository.findByAccountNumber(recipient.getAccountNumber())).thenReturn(Optional.of(recipient));
        when(accountRepository.findByAccountNumber(sender.getAccountNumber())).thenReturn(Optional.of(sender));


        assertThrows(AccountInactiveException.class, () -> accountPostService.transferViaPix(dtoPix));

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Lançar exceção se conta do cliente que envia o pix seja inativa")
    void deveLancarExcecaoSeContaDoSenderEstejaInativa(){
        Account recipient = new Account();
        recipient.addNumberAccount("2020 2020 2020 2020");

        Account sender = new Account();
        sender.addNumberAccount("1010 1010 1010 1010");
        sender.blockedAccount();

        TransferPixDTO dtoPix = new TransferPixDTO(
                "1010 1010 1010 1010",
                "2020 2020 2020 2020",
                BigDecimal.valueOf(200)
        );

        when(accountRepository.findByAccountNumber(recipient.getAccountNumber())).thenReturn(Optional.of(recipient));
        when(accountRepository.findByAccountNumber(sender.getAccountNumber())).thenReturn(Optional.of(sender));


        assertThrows(AccountInactiveException.class, () -> accountPostService.transferViaPix(dtoPix));

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Lançar exceção se valor da transferência for igual a 0")
    void deveLancarExcecaoSeValorFor0(){
        Account recipient = new Account();
        recipient.addNumberAccount("2020 2020 2020 2020");

        Account sender = new Account();
        sender.addNumberAccount("1010 1010 1010 1010");

        TransferPixDTO dtoPix = new TransferPixDTO(
                "1010 1010 1010 1010",
                "2020 2020 2020 2020",
                BigDecimal.ZERO
        );

        when(accountRepository.findByAccountNumber(recipient.getAccountNumber())).thenReturn(Optional.of(recipient));
        when(accountRepository.findByAccountNumber(sender.getAccountNumber())).thenReturn(Optional.of(sender));


        assertThrows(IllegalArgumentException.class, () -> accountPostService.transferViaPix(dtoPix));

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

    @Test
    @DisplayName("Lançar exceção se contas forem iguais")
    void deveLancarExcecaoSeContasForemIguais(){
        Account recipient = new Account();
        recipient.addNumberAccount("2020 2020 2020 2020");

        Account sender = new Account();
        sender.addNumberAccount("2020 2020 2020 2020");

        TransferPixDTO dtoPix = new TransferPixDTO(
                "2020 2020 2020 2020",
                "2020 2020 2020 2020",
                BigDecimal.valueOf(200)
        );

        when(accountRepository.findByAccountNumber(recipient.getAccountNumber())).thenReturn(Optional.of(recipient));
        when(accountRepository.findByAccountNumber(sender.getAccountNumber())).thenReturn(Optional.of(sender));


        assertThrows(PixTransferFailedException.class, () -> accountPostService.transferViaPix(dtoPix));

        verify(accountRepository, never()).save(any(Account.class));
        verifyNoInteractions(transactionRepository);
    }

}
