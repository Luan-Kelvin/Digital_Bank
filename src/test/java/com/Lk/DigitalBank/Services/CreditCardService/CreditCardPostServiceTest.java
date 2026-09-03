package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Components.NumberGenerator;
import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPostDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Exception.AccountAlreadyHasCreditCardException;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
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
public class CreditCardPostServiceTest {

    @Mock
    private Conversor conversor;

    @Mock
    private NumberGenerator numberGenerator;

    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CreditCardPostService creditCardPostService;

    @Test
    void deveCriarNovoCreditCardERetornarDTO(){
        CreditCardPostDTO postDto = new CreditCardPostDTO("2547", 1L, 20);
        Account account = new Account();
        CreditCardGetDTO getDto = new CreditCardGetDTO(1L, LocalDate.now().plusYears(5), BigDecimal.ZERO, account.getId());

        when(accountRepository.findById(postDto.idAccount())).thenReturn(Optional.of(account));
        when(numberGenerator.generateNumberCard()).thenReturn("1010 2020 4545 8888");
        when(conversor.converterCreditCard(any(CreditCard.class))).thenReturn(getDto);

        CreditCardGetDTO resultado = creditCardPostService.createCreditCard(postDto);

        ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);

        verify(creditCardRepository).save(captor.capture());

        CreditCard cardSave = captor.getValue();

        assertEquals(postDto.password(), cardSave.getPassword());
        assertEquals(postDto.closingDayInvoice(), cardSave.getClosingDayInvoice());
        assertEquals("1010 2020 4545 8888", cardSave.getCardNumber());
        assertEquals(account, cardSave.getAccount());
        assertEquals(resultado.creditLimit(), cardSave.getCreditLimit());
        assertEquals(resultado.expirationDate(), cardSave.getExpirationDate());
        assertEquals(resultado.idAccount(), cardSave.getAccount().getId());

        verify(accountRepository).findById(postDto.idAccount());
        verify(numberGenerator).generateNumberCard();
        verify(conversor).converterCreditCard(any(CreditCard.class));
    }

    @Test
    void develancarExcecaoSeAccountNaoForEncontrada(){
        CreditCardPostDTO postDto = new CreditCardPostDTO("2547", 1L, 20);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountDoesNotExistException.class, () -> creditCardPostService.createCreditCard(postDto));

        verifyNoInteractions(creditCardRepository);
        verifyNoInteractions(numberGenerator);
        verifyNoInteractions(conversor);

        verify(accountRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoCasoContaJaTenhaCartaoDeCredito(){
        Account account= new Account();
        CreditCard creditCard = new CreditCard("4444", "2222 4444 8888 7777", account, 10);
        CreditCardPostDTO postDto = new CreditCardPostDTO("2547", 1L, 20);


        account.addCreditCard(creditCard);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(AccountAlreadyHasCreditCardException.class, () -> creditCardPostService.createCreditCard(postDto));

        verifyNoInteractions(creditCardRepository);
        verifyNoInteractions(numberGenerator);
        verifyNoInteractions(conversor);

        verify(accountRepository).findById(1L);
    }
}
