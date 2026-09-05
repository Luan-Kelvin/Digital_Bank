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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReduceLimitTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private Conversor conversor;

    @InjectMocks
    private CreditCardPatchService creditCardPatchService;

    @Test
    @DisplayName("Deve reduzir limite  eretornar um dto")
    void reduzirLimiteERetornarUmDTO(){
        Customer customer = new Customer("José", "123.456.789-10", LocalDate.of(2005, 12 , 1));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2587", "1010 1010 1010 1010", account, 12);
        card.increaseLimit(BigDecimal.valueOf(300));

        CreditCardPatchLimitDTO patchDto = new CreditCardPatchLimitDTO(card.getCardNumber(), customer.getCpf(), BigDecimal.valueOf(120));
        CreditCardGetDTO getDto = new CreditCardGetDTO(card.getId(), card.getExpirationDate(), card.getCreditLimit(), card.getAccount().getId());

        when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));
        when(conversor.converterCreditCard(card)).thenReturn(getDto);

        CreditCardGetDTO resultado = creditCardPatchService.reduceLimit(patchDto);

        ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);

        verify(creditCardRepository).save(captor.capture());

        CreditCard cardSave = captor.getValue();

        assertEquals(BigDecimal.valueOf(180), cardSave.getCreditLimit());
        assertEquals(cardSave.getId(), getDto.id());

        verify(creditCardRepository).findByCardNumber(card.getCardNumber());
        verify(conversor).converterCreditCard(card);
    }

    @Test
    @DisplayName("Deve lançar exceção se credit card nao existir")
    void lancarExcecaoSenaoExistir(){
        CreditCardPatchLimitDTO patchDto = new CreditCardPatchLimitDTO("5555 5555 5555 5555", "123.456.789-10", BigDecimal.valueOf(120));

        when(creditCardRepository.findByCardNumber("5555 5555 5555 5555")).thenReturn(Optional.empty());

        assertThrows(CreditCardsNotExistException.class, () -> creditCardPatchService.reduceLimit(patchDto));

        verify(creditCardRepository).findByCardNumber("5555 5555 5555 5555");
        verify(creditCardRepository, never()).save(any(CreditCard.class));
        verify(conversor, never()).converterCreditCard(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se cartão de crédito estiver inativo")
    void lancarExcecaoSeCartaoEstiverInativo(){
        Customer customer = new Customer("José", "123.456.789-10", LocalDate.of(2005, 12 , 1));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2587", "1010 1010 1010 1010", account, 12);
        CreditCardPatchLimitDTO patchDto = new CreditCardPatchLimitDTO("5555 5555 5555 5555", "123.456.789-10", BigDecimal.valueOf(120));
        card.blockCard("2587");

        when(creditCardRepository.findByCardNumber("5555 5555 5555 5555")).thenReturn(Optional.of(card));

        assertThrows(InactiveCreditCardException.class, () -> creditCardPatchService.reduceLimit(patchDto));

        verify(creditCardRepository).findByCardNumber("5555 5555 5555 5555");
        verify(creditCardRepository, never()).save(any(CreditCard.class));
        verify(conversor, never()).converterCreditCard(any(CreditCard.class));
    }
}
