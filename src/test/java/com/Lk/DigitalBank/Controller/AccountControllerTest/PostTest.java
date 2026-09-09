package com.Lk.DigitalBank.Controller.AccountControllerTest;

import com.Lk.DigitalBank.Controller.AccountController.PostRequest.AccountPostController;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Exception.AccountAlreadyExistsException;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Services.AccountService.AccountPostService;
import com.Lk.DigitalBank.Services.AccountService.AccountServiceGeneral;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountPostController.class)
public class PostTest {

    @MockitoBean
    private AccountPostService accountPostService;

    @MockitoBean
    private AccountServiceGeneral accountServiceGeneral;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Deve retornar Status 201 - CREATED se conta for criada com sucesso!")
    void deveRetornarStatus201AposContaCriada() throws Exception {
        String json = """
                    {
                         "cpfCustomer": "123.456.789-10",
                         "accountType": "CURRENT"
                    }
                """;

        AccountGetDTO accountGetDTO = new AccountGetDTO(
                1L,
                "1000 1000 1000 1000",
                BigDecimal.valueOf(250),
                AccountType.CURRENT,
                AccountStatus.ACTIVE,
                2L,
                "Joaquim"

        );

        when(accountPostService.createAccount(any(AccountPostDTO.class))).thenReturn(accountGetDTO);

        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());

        verify(accountPostService).createAccount(any(AccountPostDTO.class));

    }

    @Test
    @DisplayName("Deve retornar 400 - BAD REQUEST se cpf for inválido")
    void retornar400SeCpfForInvalido() throws Exception {
        String json = """
                
                {
                     "cpfCustomer": "12345678910",
                     "accountType": "CURRENT"
                }
                
                """;


        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());

        verifyNoInteractions(accountPostService);
    }

    @Test
    @DisplayName("Deve retornar 404 - Not FOUND se cpf nao pertencer a nenhum cliente do banco.")
    void deveRetornar404SeCpfNaoExistir() throws Exception {
        String json = """
                
                {
                   "cpfCustomer": "123.456.789-10",
                   "accountType": "CURRENT" 
                }
                
                """;

        doThrow(new CustomerDoesNotExistException("ERRO! Cpf não pertence a nenhum cliente."))
                .when(accountPostService).createAccount(any(AccountPostDTO.class));

        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isNotFound());

        verify(accountPostService).createAccount(any(AccountPostDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 409 - CONFLICT se Cliente ja possui uma conta com o mesmo tipo")
    void deveLancar409SeContaJaExistir() throws Exception {
        String json = """
                
                {
                    "cpfCustomer": "123.456.789-10",
                    "accountType": "CURRENT"
                }
                
                """;

        doThrow(new AccountAlreadyExistsException("ERRO! Conta já existe."))
                .when(accountPostService).createAccount(any(AccountPostDTO.class));

        mvc.perform(
                post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isConflict());

        verify(accountPostService).createAccount(any(AccountPostDTO.class));
    }

}
