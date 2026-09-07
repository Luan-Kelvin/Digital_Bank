package com.Lk.DigitalBank.Controller.AccountControllerTest;

import com.Lk.DigitalBank.Controller.AccountController.DeleteRequest.AccountDeleteController;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Services.AccountService.AccountDeleteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDeleteController.class)
public class DeleteTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AccountDeleteService accountDeleteService;

    @Test
    @DisplayName("Deve retornar Status 204 - NO CONTENT se ocorrer tudo bem no método.")
    void deveRetornar204NoContentSeOcorrerTudoBem() throws Exception {
        doNothing()
                .when(accountDeleteService)
                        .deleteAccount("12345");

        mvc.perform(delete("/accounts/delete/12345")).
                andExpect(status().isNoContent());

        verify(accountDeleteService).deleteAccount("12345");

    }

    @Test
    @DisplayName("Deve retornar Status 404 - NOT FOUND se Conta não existir")
    void deveRetornar404SeCntaNaoExistir() throws Exception {

        doThrow(new AccountDoesNotExistException("Conta não encontrada."))
                .when(accountDeleteService)
                        .deleteAccount("12345");

        mvc.perform(delete("/accounts/delete/12345")).andExpect(status().isNotFound());

        verify(accountDeleteService).deleteAccount("12345");
    }

    @Test
    @DisplayName("Deve retornar Status 409 - CONFLICT se conta estiver inativa.")
    void deveLancar409SeContaEstiverInativa() throws Exception {
        doThrow(new AccountInactiveException("Conta está inativa."))
                .when(accountDeleteService).deleteAccount("12345");

        mvc.perform(delete("/accounts/delete/12345"))
                        .andExpect(status().isConflict());

        verify(accountDeleteService).deleteAccount("12345");
    }
}
