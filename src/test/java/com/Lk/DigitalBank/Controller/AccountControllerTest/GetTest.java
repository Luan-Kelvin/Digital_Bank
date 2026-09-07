package com.Lk.DigitalBank.Controller.AccountControllerTest;

import com.Lk.DigitalBank.Controller.AccountController.GetRequest.AccountGetController;
import com.Lk.DigitalBank.DTOs.Account.AccountBalanceDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Exception.AccountInactiveException;
import com.Lk.DigitalBank.Exception.InvalidAccountStatusException;
import com.Lk.DigitalBank.Exception.InvalidAccountTypeException;
import com.Lk.DigitalBank.Services.AccountService.AccountGetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountGetController.class)
public class GetTest {

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

    @Test
    @DisplayName("Deve retornar Status 200 - OK se encontrar a contapor ID.")
    void deveRetornar200NaBuscaPorIdDaConta() throws Exception {
        when(accountGetService.findById(1L)).thenReturn(any(AccountGetDTO.class));

        mvc.perform(get("/accounts/id/1")).andExpect(status().isOk());

        verify(accountGetService).findById(1L);

    }

    @Test
    @DisplayName("Deve retornar status 404 - NOT FOUND Se id da conta não for encontrada.")
    void deveRetornar404SeIdDaContaNaoExistir() throws Exception {
        doThrow(new AccountDoesNotExistException("ERRO! Conta não exise."))
                .when(accountGetService).findById(1L);

        mvc.perform(get("/accounts/id/1")).andExpect(status().isNotFound());

        verify(accountGetService).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Status 200 - OK se retornar o saldo da conta solicitada.")
    void deveRetornar200AposConsultaDeSaldo() throws Exception {
        when(accountGetService.checkBalance("12345")).thenReturn(any(AccountBalanceDTO.class));

        mvc.perform(get("/accounts/saldo/12345")).andExpect(status().isOk());

        verify(accountGetService).checkBalance("12345");
    }

    @Test
    @DisplayName("Deve retornar Status 404 - NOT FOUND se conta não for encontrda.")
    void deveRetornar404SeNaoLocalizarContaParaVerSaldo() throws Exception {
        doThrow(new AccountDoesNotExistException("ERRO! Conta não encontrada"))
                .when(accountGetService)
                .checkBalance("12345");

        mvc.perform(get("/accounts/saldo/12345")).andExpect(status().isNotFound());

        verify(accountGetService).checkBalance("12345");
    }

    @Test
    @DisplayName("Deve retornar Status 409 - CONFLICT se conta não estiver ativa para consulta de saldo")
    void deveRetornar409SeContaEstiverInativaParaVerSaldo() throws Exception {
        doThrow(new AccountInactiveException("ERRO! Conta não está ativa"))
                .when(accountGetService)
                .checkBalance("12345");

        mvc.perform(get("/accounts/saldo/12345")).andExpect(status().isConflict());

        verify(accountGetService).checkBalance("12345");
    }

    @Test
    @DisplayName("Deve retornar Status 200 quando encontrar conta com o mesmo número passado por parametro.")
    void deveRetrnarStatus200SeContaForEncontrada() throws Exception {
        when(accountGetService.findByAccountNumber("12345")).thenReturn(any(AccountGetDTO.class));

        mvc.perform(get("/accounts/number/12345")).andExpect(status().isOk());

        verify(accountGetService).findByAccountNumber("12345");
    }

    @Test
    @DisplayName("Deve retornar status 404 - NOT FOUND se Cnt não com número solicitado não for encontrado.")
    void retornar404SeContaComNumeroSolicitadoNaoExistir() throws Exception {
        doThrow(new AccountDoesNotExistException("ERRO! Conta não existe."))
                .when(accountGetService)
                .findByAccountNumber("12345");

        mvc.perform(get("/accounts/number/12345")).andExpect(status().isNotFound());

        verify(accountGetService).findByAccountNumber("12345");
    }

    @Test
    @DisplayName("Deve retornar status 409 - CONFLICT se Conta com número solicitado estiver inativa.")
    void retornar404SeContaComNumeroSolicitadoEstiverInativa() throws Exception {
        doThrow(new AccountInactiveException( "ERRO! Conta inativa."))
                .when(accountGetService)
                .findByAccountNumber("12345");

        mvc.perform(get("/accounts/number/12345")).andExpect(status().isConflict());

        verify(accountGetService).findByAccountNumber("12345");
    }

    @Test
    @DisplayName("Deve retornar status 200 - OK quando encontrar a lista de contas com status solicitado.")
    void deveRetronarListaDeContasComStatusSolicitado() throws Exception {
        List<AccountGetDTO> list = new ArrayList<>();
        when(accountGetService.searchByStatus("status")).thenReturn(list);

        mvc.perform(get("/accounts/status/status")).andExpect(status().isOk());

        verify(accountGetService).searchByStatus("status");
    }

    @Test
    @DisplayName("Deve retornar Status 409 - CONFLICT se o status passado for inválido")
    void deveRetornarStatus409SeStatusPassadoNoEndpointForInvalido() throws Exception {
        doThrow(new InvalidAccountStatusException("ERRO! Status passado é inválido."))
                .when(accountGetService).searchByStatus("status");

        mvc.perform(get("/accounts/status/status")).andExpect(status().isConflict());

        verify(accountGetService).searchByStatus("status");
    }

    @Test
    @DisplayName("Deve retornar Statuys 200 - OK quando encontrar a lista com Type passado pelo endpoint")
    void deveRetornarStatus200SeListaComTypeForEcontrada() throws Exception {
        List<AccountGetDTO> list = new ArrayList<>();
        when(accountGetService.searchByType("type")).thenReturn(list);

        mvc.perform(get("/accounts/type/type")).andExpect(status().isOk());

        verify(accountGetService).searchByType("type");
    }

    @Test
    @DisplayName("Deve retornar Status 409 - CONFLICT se type passado pelo endpoint for inválido")
    void deveRetornarStatus409SeTypeForInvalido() throws Exception {
        doThrow(new InvalidAccountTypeException("ERRO! Type de conta passado por parâmetro não existe."))
                .when(accountGetService).searchByType("type");

        mvc.perform(get("/accounts/type/type")).andExpect(status().isConflict());

        verify(accountGetService).searchByType("type");
    }


}
