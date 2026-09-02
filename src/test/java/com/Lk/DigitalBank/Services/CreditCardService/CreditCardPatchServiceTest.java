package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Exception.InvalidPasswordException;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditCardPatchServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardPatchService creditCardPatchService;

    @Test
    void deveVerificarSeoStatusDoCartaoMudaParaBlocked(){
        Customer customer = new Customer("Jandre", "123.456.789-10", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        String number = "1234 5678 9101 1213";
        CreditCard creditCard = new CreditCard("4555", number, account, 25);

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO(number, "123.456.789-10", "4555");

        when(creditCardRepository.findByCardNumber(number)).thenReturn(Optional.of(creditCard));

        creditCardPatchService.blockCard(dto);

        assertEquals(CardStatus.BLOCKED, creditCard.getCardStatus());

        verify(creditCardRepository).findByCardNumber(number);
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    void deveLancarExcecaoSeCPFForDIferente(){
        Customer customer = new Customer("Jandre", "545.789.888-25", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("4555", "5412 2587 7897 5454", account, 25);

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO("5412 2587 7897 5454", "123.456.789-10", "4555");

        when(creditCardRepository.findByCardNumber(dto.number())).thenReturn(Optional.of(creditCard));

        assertThrows(InvalidCPFException.class, () -> creditCardPatchService.blockCard(dto));

        verify(creditCardRepository).findByCardNumber(dto.number());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    void deveLancarExcecaoSeSenhaForDiferente(){
        Customer customer = new Customer("Jandre", "123.456.789-10", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("4555", "5412 2587 7897 5454", account, 25);

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO("5412 2587 7897 5454", "123.456.789-10", "4325");

        when(creditCardRepository.findByCardNumber(dto.number())).thenReturn(Optional.of(creditCard));

        assertThrows(InvalidPasswordException.class, () -> creditCardPatchService.blockCard(dto));

        verify(creditCardRepository).findByCardNumber(dto.number());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }
}
