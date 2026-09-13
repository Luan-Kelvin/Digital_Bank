package com.Lk.DigitalBank.Controller.AccountControllerTest.IntegrationTest;

import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Services.AccountService.AccountPostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateAccountTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AccountPostService accountPostService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("Deve criar uma nova conta.")
    void deveCriarNovaCOnta() throws Exception {
        Customer customer = new Customer("Paulo Félix", "123.456.789-12", LocalDate.of(2002, 12, 25));
        customerRepository.save(customer);

        String json = """
                {
                    "cpfCustomer": "123.456.789-12",
                    "accountType": "CURRENT"
                }
                """;


        MvcResult result = mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated()).andReturn();

        AccountGetDTO dto = mapper.readValue(result.getResponse().getContentAsString(), AccountGetDTO.class);

        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow();

        assertEquals(dto.accountNumber(), account.getAccountNumber());
        assertEquals(dto.accountType(), account.getAccountType());
        assertEquals(customer.getCpf(), account.getCustomer().getCpf());
    }
}
