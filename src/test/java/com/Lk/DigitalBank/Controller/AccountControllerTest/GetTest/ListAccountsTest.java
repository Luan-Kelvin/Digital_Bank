package com.Lk.DigitalBank.Controller.AccountControllerTest.GetTest;

import com.Lk.DigitalBank.Controller.AccountController.GetRequest.AccountGetController;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.Services.AccountService.AccountGetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountGetController.class)
public class ListAccountsTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AccountGetService accountGetService;

    @Test
    @DisplayName("Deve retornar Status 200 se encontrar uma lista na busca de contas ativas")
    void deveRetornar200ParaBuscaDeContasAtivas() throws Exception {
        List<AccountGetDTO> list = new ArrayList<>();

        when(accountGetService.listAccountsAcitives()).thenReturn(list);

        mvc.perform(get("/accounts")).andExpect(status().isOk());

        verify(accountGetService).listAccountsAcitives();
    }

    @Test
    @DisplayName("Deve retornar Status 200 se encontrar uma lista na busca por listas inativas.")
    void deveRetornar200ParaBuscaDeListaInativas() throws Exception {
        List<AccountGetDTO> list = new ArrayList<>();

        when(accountGetService.listAccountsInactive()).thenReturn(list);

        mvc.perform(get("/accounts/inativas")).andExpect(status().isOk());

        verify(accountGetService).listAccountsInactive();
    }

}
