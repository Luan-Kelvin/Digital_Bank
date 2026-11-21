package com.Lk.DigitalBank.Controller.AccountEventsService;


import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsGetDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsPostDTO;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.ENUM.EventType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Services.AccountEvents.AccountEventsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AccountEventsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountEventsService accountEventsService;

    @Autowired
    private AccountEventsRepository accountEventsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    Long idAccount1;
    Long idAccount2;

    @BeforeEach
    void setup(){
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

        Account account1 = accountRepository.findById(idAccount1).orElse(null);
        Account account2 = accountRepository.findById(idAccount2).orElse(null);

        assertThat(account1.getAccountEvents().size()).isEqualTo(3);
        assertThat(account2.getAccountEvents().size()).isEqualTo(1);

        assertThat(getDto1.sequence()).isEqualTo(0);
        assertThat(getDto2.sequence()).isEqualTo(1);
        assertThat(getDto3.sequence()).isEqualTo(2);
        assertThat(getDto4.sequence()).isEqualTo(0);

        assertThat(getDto1.id_account()).isEqualTo(idAccount1);
        assertThat(getDto4.id_account()).isEqualTo(idAccount2);

    }



    // CRIAR EVENTS POTS PARA TESTE
    private AccountEventsPostDTO createAccount(Long id, BigDecimal value, EventType type){
        return new AccountEventsPostDTO(
                id,
                type,
                value
        );
    }

}