package com.Lk.DigitalBank.Controller.CreditCardCntrollerTest;

import com.Lk.DigitalBank.Controller.CreditCardController.GetRequest.CreditCardGetController;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardGetService;
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

@WebMvcTest(CreditCardGetController.class)
public class GetTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreditCardGetService creditCardGetService;

    @Test
    @DisplayName("Deve retornar status 200 - OK quando devolver lista de cartões ativos")
    void deveRetornarOkParaListagemDeCartoesAtivos() throws Exception {
        List<CreditCardGetDTO> dto = new ArrayList<>();
        when(creditCardGetService.listCreditCards()).thenReturn(dto);

        mvc.perform(get("/cards")).andExpect(status().isOk());

        verify(creditCardGetService).listCreditCards();
    }


    @Test
    @DisplayName("Deve retornar status 200 - OK quando devolver lista de cartões bloqueados")
    void deveRetornarOkParaListagemDeCartoesBloqueados() throws Exception {
        List<CreditCardGetDTO> dto = new ArrayList<>();
        when(creditCardGetService.listCreditCardsBlockeds()).thenReturn(dto);

        mvc.perform(get("/cards/bloqueados")).andExpect(status().isOk());

        verify(creditCardGetService).listCreditCardsBlockeds();
    }

    @Test
    @DisplayName("Deve retornar status 200 - OK quando devolver lista de cartões expirados")
    void deveRetornarOkParaListagemDeCartoesExpirados() throws Exception {
        List<CreditCardGetDTO> dto = new ArrayList<>();
        when(creditCardGetService.listCreditCardExpired()).thenReturn(dto);

        mvc.perform(get("/cards/expirados")).andExpect(status().isOk());

        verify(creditCardGetService).listCreditCardExpired();
    }

    @Test
    @DisplayName("Deve retornar status 200 - OK quando devolver lista de cartões cancelados")
    void deveRetornarOkParaListagemDeCartoesCancelados() throws Exception {
        List<CreditCardGetDTO> dto = new ArrayList<>();
        when(creditCardGetService.listCreditCardCanceled()).thenReturn(dto);

        mvc.perform(get("/cards/cancelados")).andExpect(status().isOk());

        verify(creditCardGetService).listCreditCardCanceled();
    }
}

