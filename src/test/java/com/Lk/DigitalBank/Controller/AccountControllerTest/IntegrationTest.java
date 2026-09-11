package com.Lk.DigitalBank.Controller.AccountControllerTest;

import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Entity.Transaction;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Repository.TransactionRepository;
import com.Lk.DigitalBank.Services.AccountService.AccountPostService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountPostService accountPostService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    @DisplayName("Deve realizar a trnsferência pix, criar as transições e associa-las as suas contas.")
    @Transactional
    void deveRealizartransferenciapix() throws Exception {
        Customer customer1 = new Customer("Rodney", "123.456.789-85", LocalDate.of(1995, 3, 15));
        Customer customer2 = new Customer("Bruno Henrique", "235.456.789-10", LocalDate.of(1999, 4, 27));

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        Account accountSender = new Account(customer1, AccountType.CURRENT);
        accountSender.addNumberAccount("1010 1010 1010 1010");
        accountSender.deposit(BigDecimal.valueOf(500));

        Account accountRecipient = new Account(customer2, AccountType.CURRENT);
        accountRecipient.addNumberAccount("2020 2020 2020 2020");

        accountRepository.save(accountSender);
        accountRepository.save(accountRecipient);

        String json = """
                {
                    "senderAccount": "1010 1010 1010 1010",
                    "recipientAccount": "2020 2020 2020 2020",
                    "value": 200
                }       
                """;

        mvc.perform(
                post("/accounts/pix")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)

        ).andExpect(status().isOk());

        List<Transaction> listTransactions = transactionRepository.findAll();

        Account senderAfterPix =
                accountRepository.findByAccountNumber("1010 1010 1010 1010")
                        .orElseThrow();

        Account recipientAfterPix =
                accountRepository.findByAccountNumber("2020 2020 2020 2020")
                        .orElseThrow();

        assertEquals(new BigDecimal("300"), senderAfterPix.getBalance());
        assertEquals(new BigDecimal("200"), recipientAfterPix.getBalance());
        assertEquals(2, listTransactions.size());
        assertEquals(listTransactions.get(0).getAccount().getAccountNumber(), senderAfterPix.getAccountNumber());
        assertEquals(listTransactions.get(1).getAccount().getAccountNumber(), recipientAfterPix.getAccountNumber());

    }

}
