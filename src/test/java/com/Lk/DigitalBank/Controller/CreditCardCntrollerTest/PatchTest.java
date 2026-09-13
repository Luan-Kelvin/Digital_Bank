package com.Lk.DigitalBank.Controller.CreditCardCntrollerTest;

import com.Lk.DigitalBank.Controller.CreditCardController.PatchRequest.CreditCardPatchController;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Exception.InactiveCreditCardException;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Exception.InvalidPasswordException;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditCardPatchController.class)
public class PatchTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreditCardPatchService creditCardPatchService;

    @Test
    @DisplayName("Deve retornar Status 204 - NO CONTENT quando cartão for bloqueado")
    void deveRetornar204QuandocartaoForBloqueado() throws Exception {
        String json = """
                {
                    "number": "1010 1010 1010 1010",
                    "cpf": "123.456.789-10",
                    "password": "1241"
                }
                """;

        mvc.perform(
                patch("/card/bloquear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isNoContent());

        verify(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));
    }

    @Test
    @DisplayName("Deve lançar status 404 - NOT FOUND se cartão de crédito nao existir para bloqueio.")
    void lanca404SeCartaoDeCreditoNaoExistirParaBloqueio() throws Exception {
        String json = """
                {
                    "number": "1010 1010 1010 1010",
                    "cpf": "123.456.789-10",
                    "password": "1241"
                }
                """;

        doThrow(new CreditCardsNotExistException("ERRO! Cartão de crédito não existe."))
                .when(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));

        mvc.perform(
                patch("/card/bloquear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isNotFound());

        verify(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));
    }

    @Test
    @DisplayName("Deve lançar status 409 - CONFLICT se cartão de crédito nao existir para bloqueio.")
    void lanca409SeCartaoEstiverInativo() throws Exception {
        String json = """
                {
                    "number": "1010 1010 1010 1010",
                    "cpf": "123.456.789-10",
                    "password": "1241"
                }
                """;

        doThrow(new InactiveCreditCardException("ERRO! Cartão de crédito inativo."))
                .when(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));

        mvc.perform(
                patch("/card/bloquear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isConflict());

        verify(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));
    }

    @Test
    @DisplayName("Deve lançar status 409 - CONFLICT se cpf for inválido.")
    void lanca409SeCpfForInvalido() throws Exception {
        String json = """
                {
                    "number": "1010 1010 1010 1010",
                    "cpf": "123.456.789-10",
                    "password": "1241"
                }
                """;

        doThrow(new InvalidCPFException("ERRO! CPF Inválido."))
                .when(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));

        mvc.perform(
                patch("/card/bloquear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isConflict());

        verify(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));
    }

    @Test
    @DisplayName("Deve lançar status 400 - BAD REQUEST se senha for inválida.")
    void lanca400SeSenhaForInvalida() throws Exception {
        String json = """
                {
                    "number": "1010 1010 1010 1010",
                    "cpf": "123.456.789-10",
                    "password": "1241"
                }
                """;

        doThrow(new InvalidPasswordException("ERRO! senha inválida."))
                .when(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));

        mvc.perform(
                patch("/card/bloquear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isBadRequest());

        verify(creditCardPatchService).blockCard(any(CreditCardPatchBlockedDTO.class));
    }


}
