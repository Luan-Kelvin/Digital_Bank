package com.Lk.DigitalBank.Controller.CreditCardCntrollerTest;

import com.Lk.DigitalBank.Controller.CreditCardController.PostRequest.CreditCardPostController;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPostDTO;
import com.Lk.DigitalBank.Exception.AccountAlreadyHasCreditCardException;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardPostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreditCardPostController.class)
public class PostTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreditCardPostService creditCardPostService;

    @Test
    @DisplayName("Deve retornar status 201 - CREATED quando conta for criada.")
    void deveRetornar201SeContaForCriada() throws Exception {
        String json = """
                {
                    "password": "1010",
                    "idAccount": 1,
                    "closingDayInvoice": 12
                }
                """;

        CreditCardGetDTO getDto = new CreditCardGetDTO(
                1L,
                LocalDate.of(2027, 12, 12),
                BigDecimal.valueOf(500),
                1L);

        when(creditCardPostService.createCreditCard(any(CreditCardPostDTO.class))).thenReturn(getDto);

        mvc.perform(
                post("/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());

        verify(creditCardPostService).createCreditCard(any(CreditCardPostDTO.class));
    }

    @Test
    @DisplayName("Deve retornar Status 409 - CONFLICT se conta ja existir")
    void deveRetornar409SeContaExistir() throws Exception {
        String json = """
                {
                    "password": "1010",
                    "idAccount": 1,
                    "closingDayInvoice": 12
                }
                """;

        doThrow(new AccountAlreadyHasCreditCardException("ERRO! COnta ja existe."))
                .when(creditCardPostService).createCreditCard(any(CreditCardPostDTO.class));

        mvc.perform(
                post("/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isConflict());

        verify(creditCardPostService).createCreditCard(any(CreditCardPostDTO.class));


    }
}
