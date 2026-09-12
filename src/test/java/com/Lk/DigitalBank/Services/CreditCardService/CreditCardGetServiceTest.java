package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditCardGetServiceTest {

    @Mock
    private Conversor conversor;

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardGetService creditCardGetService;

    @Test
    @DisplayName("Deve listar todos os cartões de credito que estão ativos.")
    void listarCartoesAtivos(){
        CreditCard card1 = new CreditCard();
        CreditCard card2 = new CreditCard();
        List<CreditCard> cards = List.of(card1, card2);

        when(creditCardRepository.searchCreditCardActive()).thenReturn(cards);

        List<CreditCardGetDTO> dtos = creditCardGetService.listCreditCards();

        verify(creditCardRepository).searchCreditCardActive();

        assertEquals(2, dtos.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não tiver cartões ativos")
    void deveRetornarListaVaziaSeNaoTiverCardsAtivos(){
        when(creditCardRepository.searchCreditCardActive()).thenReturn(List.of());

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCards();

        verify(creditCardRepository).searchCreditCardActive();

        assertEquals(0, dto.size());
    }

    @Test
    @DisplayName("Deve devolver lista com cartões bloqueados")
    void listarCartoesBloqueados(){
        CreditCard card1 = new CreditCard();
        CreditCard card2 = new CreditCard();

        List<CreditCard> cards = List.of(card1, card2);

        when(creditCardRepository.searchCreditCardBlocked()).thenReturn(cards);

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCardsBlockeds();

        verify(creditCardRepository).searchCreditCardBlocked();

        assertEquals(2, dto.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não tiver cartões bloquados")
    void deveRetornarListaVaziaSeNaoTiverCardsBloqueados(){
        when(creditCardRepository.searchCreditCardBlocked()).thenReturn(List.of());

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCardsBlockeds();

        verify(creditCardRepository).searchCreditCardBlocked();

        assertEquals(0, dto.size());
    }

    @Test
    @DisplayName("Deve devolver lista com cartões expirados")
    void listarCartoesExpirados(){
        CreditCard card1 = new CreditCard();
        CreditCard card2 = new CreditCard();

        List<CreditCard> cards = List.of(card1, card2);

        when(creditCardRepository.searchCreditCardExpired()).thenReturn(cards);

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCardExpired();

        verify(creditCardRepository).searchCreditCardExpired();

        assertEquals(2, dto.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não tiver cartões expirados")
    void deveRetornarListaVaziaSeNaoTiverCardsExpirados(){
        when(creditCardRepository.searchCreditCardExpired()).thenReturn(List.of());

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCardExpired();

        verify(creditCardRepository).searchCreditCardExpired();

        assertEquals(0, dto.size());
    }

    @Test
    @DisplayName("Deve devolver lista com cartões cancelados")
    void listarCartoesCancelados(){
        CreditCard card1 = new CreditCard();
        CreditCard card2 = new CreditCard();

        List<CreditCard> cards = List.of(card1, card2);

        when(creditCardRepository.searchCreditCardCanceled()).thenReturn(cards);

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCardCanceled();

        verify(creditCardRepository).searchCreditCardCanceled();

        assertEquals(2, dto.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não tiver cartões cancelados")
    void deveRetornarListaVaziaSeNaoTiverCardsCancelados(){
        when(creditCardRepository.searchCreditCardCanceled()).thenReturn(List.of());

        List<CreditCardGetDTO> dto = creditCardGetService.listCreditCardCanceled();

        verify(creditCardRepository).searchCreditCardCanceled();

        assertEquals(0, dto.size());
    }
}
