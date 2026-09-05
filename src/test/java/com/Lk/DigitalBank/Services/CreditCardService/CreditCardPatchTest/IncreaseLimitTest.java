package com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchTest;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchLimitDTO;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Exception.InactiveCreditCardException;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncreaseLimitTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private Conversor conversor;

    @InjectMocks
    private CreditCardPatchService creditCardPatchService;

    @Test
    @DisplayName("Deve aumentar limite do cartão e retornar ele convertido em DTO.")
    void deveAumenraLimiteDoCartao(){
        Customer customer = new Customer("Luqinhas", "123.456.789-16", LocalDate.of(1998, 2, 2));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2235", "2541 5555 5555 5555", account, 14);
        CreditCardGetDTO getDto = new CreditCardGetDTO(card.getId(), card.getExpirationDate(), card.getCreditLimit(), card.getAccount().getId());

        CreditCardPatchLimitDTO patchDto = new CreditCardPatchLimitDTO(card.getCardNumber(),  customer.getCpf(), BigDecimal.valueOf(200));

        when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));
        when(conversor.converterCreditCard(card)).thenReturn(getDto);

        CreditCardGetDTO resultado = creditCardPatchService.increaseLimit(patchDto);

        ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);

        verify(creditCardRepository).save(captor.capture());

        CreditCard cardSave = captor.getValue();

        assertEquals(BigDecimal.valueOf(200), cardSave.getCreditLimit());
        assertEquals(card.getId(), resultado.id());
        assertEquals(getDto.id(), cardSave.getId());

        verify(creditCardRepository).findByCardNumber(card.getCardNumber());
        verify(conversor).converterCreditCard(card);
    }


    @Test
    @DisplayName("Deve Lancar Exceção se cartão de crédito não existir na hora do aumento de limite")
    void lncaExcecaoSecartaoNaoExistirParaAumentoDeLimite(){
        CreditCardPatchLimitDTO patchDto = new CreditCardPatchLimitDTO(
                "5555 5555 5555 5555",
                "123.456.789-10",
                BigDecimal.valueOf(200)
        );

        when(creditCardRepository.findByCardNumber("5555 5555 5555 5555")).thenReturn(Optional.empty());

        assertThrows(CreditCardsNotExistException.class, () -> creditCardPatchService.increaseLimit(patchDto));

        verify(creditCardRepository).findByCardNumber("5555 5555 5555 5555");
        verify(creditCardRepository, never()).save(any(CreditCard.class));
        verify(conversor, never()).converterCreditCard(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lancar excecao se cartão de crédito estiver inativo.")
    void lancaExcecaoParaCartaoInativoDuranteAumentoDeLimite(){
        CreditCardPatchLimitDTO patchDto = new CreditCardPatchLimitDTO(
                "5555 5555 5555 5555",
                "123.456.789-10",
                BigDecimal.valueOf(200)
        );

        Customer customer = new Customer("Luqinhas", "123.456.789-16", LocalDate.of(1998, 2, 2));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2235", "5555 5555 5555 5555", account, 14);
        card.blockCard("2235");


        when(creditCardRepository.findByCardNumber("5555 5555 5555 5555")).thenReturn(Optional.of(card));

        assertThrows(InactiveCreditCardException.class, () -> creditCardPatchService.increaseLimit(patchDto));

        verify(creditCardRepository).findByCardNumber("5555 5555 5555 5555");
        verify(creditCardRepository, never()).save(any(CreditCard.class));
        verify(conversor, never()).converterCreditCard(any(CreditCard.class));
    }
}
