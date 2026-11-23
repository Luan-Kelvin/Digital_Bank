package com.Lk.DigitalBank.Controller.AccountEventsService;


import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsGetDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsPostDTO;
import com.Lk.DigitalBank.DTOs.Account.DepositAndWithDrawAccountDTO;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.ENUM.EventType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.AccountEvents;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.InsufficientBalanceWithdraw;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Repository.TransactionRepository;
import com.Lk.DigitalBank.Services.AccountEvents.AccountEventsService;
import com.Lk.DigitalBank.Services.AccountService.AccountServiceGeneral;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class AccountEventsIntegrationTest {

    @Autowired
    private AccountEventsService accountEventsService;

    @Autowired
    private AccountServiceGeneral accountServiceGeneral;

    @Autowired
    private AccountEventsRepository accountEventsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    Long idAccount1;
    Long idAccount2;

    @BeforeEach
    void setup(){
        accountEventsRepository.deleteAll();
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();

        Customer customer1 = new Customer("Janaina", "111.111.111-11", LocalDate.of(2004, 2, 24));
        Customer customer2 = new Customer("Joselinto", "211.111.111-11", LocalDate.of(2005, 5, 5));

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        Account account1 = new Account(customer1, AccountType.CURRENT);
        account1.addNumberAccount("12345");
        accountRepository.save(account1);

        Account account2 = new Account(customer2, AccountType.CURRENT);
        account2.addNumberAccount("54321");
        accountRepository.save(account2);

        idAccount1 = account1.getId();
        idAccount2 = account2.getId();
    }

    @Test
    @DisplayName("Deve registrar eventos nas contas sem repetr o sequenceNumber")
    void registerEventsTest(){
        AccountEventsPostDTO event1 = createAccount(idAccount1, BigDecimal.valueOf(300), EventType.MONEY_DEPOSITED);
        AccountEventsPostDTO event2 = createAccount(idAccount1, BigDecimal.valueOf(200), EventType.MONEY_DEPOSITED);
        AccountEventsPostDTO event3 = createAccount(idAccount1, BigDecimal.valueOf(100), EventType.MONEY_DEPOSITED);

        AccountEventsPostDTO event4 = createAccount(idAccount2, BigDecimal.valueOf(100), EventType.MONEY_DEPOSITED);

        AccountEventsGetDTO getDto1 = accountEventsService.registerEvents(event1);
        AccountEventsGetDTO getDto2 = accountEventsService.registerEvents(event2);
        AccountEventsGetDTO getDto3 = accountEventsService.registerEvents(event3);
        AccountEventsGetDTO getDto4 = accountEventsService.registerEvents(event4);

        assertThat(eventsForAccount(idAccount1)).hasSize(3);
        assertThat(eventsForAccount(idAccount2)).hasSize(1);

        assertThat(getDto1.sequence()).isEqualTo(0);
        assertThat(getDto2.sequence()).isEqualTo(1);
        assertThat(getDto3.sequence()).isEqualTo(2);
        assertThat(getDto4.sequence()).isEqualTo(0);

        assertThat(getDto1.id_account()).isEqualTo(idAccount1);
        assertThat(getDto4.id_account()).isEqualTo(idAccount2);
    }

    @Test
    @DisplayName("Deve aplicar depósitos e saque ao saldo e gerar eventos sequenciais")
    void depositAndWithdrawUpdateBalanceAndRegisterSequentialEvents(){
        assertBalance(idAccount1, 0);

        accountServiceGeneral.deposit(createMoneyMovement("12345", 300));
        assertBalance(idAccount1, 300);

        accountServiceGeneral.deposit(createMoneyMovement("12345", 200));
        assertBalance(idAccount1, 500);

        accountServiceGeneral.withdraw(createMoneyMovement("12345", 100));
        assertBalance(idAccount1, 400);

        List<AccountEvents> events = eventsForAccount(idAccount1);
        assertThat(events).hasSize(3);
        assertThat(events)
                .extracting(AccountEvents::getSequenceNumber)
                .containsExactly(0L, 1L, 2L);
        assertThat(events)
                .extracting(AccountEvents::getEventType)
                .containsExactly(
                        EventType.MONEY_DEPOSITED,
                        EventType.MONEY_DEPOSITED,
                        EventType.MONEY_WITHDRAWN
                );
    }

    @Test
    @DisplayName("Saque acima do saldo deve falhar sem alterar o saldo ou persistir evento")
    void withdrawMoreThanBalanceDoesNotChangeBalanceOrPersistEvent(){
        accountServiceGeneral.deposit(createMoneyMovement("12345", 400));

        assertThatExceptionOfType(InsufficientBalanceWithdraw.class)
                .isThrownBy(() -> accountServiceGeneral.withdraw(createMoneyMovement("12345", 500)));

        assertBalance(idAccount1, 400);
        assertThat(eventsForAccount(idAccount1))
                .noneMatch(event -> event.getEventType() == EventType.MONEY_WITHDRAWN);
    }

    private AccountEventsPostDTO createAccount(Long id, BigDecimal value, EventType type){
        return new AccountEventsPostDTO(
                id,
                type,
                value
        );
    }

    private DepositAndWithDrawAccountDTO createMoneyMovement(String accountNumber, long value){
        return new DepositAndWithDrawAccountDTO(accountNumber, BigDecimal.valueOf(value));
    }

    private void assertBalance(Long accountId, long expectedBalance){
        Account account = accountRepository.findById(accountId).orElseThrow();
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(expectedBalance));
    }

    private List<AccountEvents> eventsForAccount(Long accountId){
        return accountEventsRepository.findAll().stream()
                .filter(event -> event.getAccount().getId().equals(accountId))
                .sorted(Comparator.comparing(AccountEvents::getSequenceNumber))
                .toList();
    }

}