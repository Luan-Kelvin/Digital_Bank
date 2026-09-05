package com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchTest;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.ChangeClosingInvoicePatchDTO;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeInvoiceClosingTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardPatchService creditCardPatchService;

    @Test
    @DisplayName("Deve alterar data de fechamento de fatura do cartão solicitado.")
    void mudarDataDeFechamento(){
        Customer customer = new Customer("Joaquim", "123.456.789-10", LocalDate.of(2001, 5, 5));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("2212", "1000 1000 1000 1000", account, 10);

        ChangeClosingInvoicePatchDTO dto = new ChangeClosingInvoicePatchDTO("1000 1000 1000 1000", customer.getCpf(), 18);

        when(creditCardRepository.findByCardNumber("1000 1000 1000 1000")).thenReturn(Optional.of(creditCard));

        creditCardPatchService.changeInvoiceClosing(dto);

        ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);

        verify(creditCardRepository).save(captor.capture());

        CreditCard cardSave = captor.getValue();

        assertEquals(dto.newDay(), cardSave.getClosingDayInvoice());

        verify(creditCardRepository).findByCardNumber("1000 1000 1000 1000");
    }

    @Test
    @DisplayName("Deve lançar exceção se cartão de crédito não existir.")
    void lancarExcecaoSeCartaoNaoExistir(){
        ChangeClosingInvoicePatchDTO dto = new ChangeClosingInvoicePatchDTO("1000 1000 1000 1000", "123.456.789-10",  18);

        when(creditCardRepository.findByCardNumber(dto.cardNumber())).thenReturn(Optional.empty());

        assertThrows(CreditCardsNotExistException.class, () -> creditCardPatchService.changeInvoiceClosing(dto));

        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se crtão de crédito estiver inátivo")
    void lancarExcecaoSeCartaoEstiverInativo(){
        Customer customer = new Customer("Joaquim", "123.456.789-10", LocalDate.of(2001, 5, 5));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("2212", "1000 1000 1000 1000", account, 10);
        creditCard.blockCard("2212");

        ChangeClosingInvoicePatchDTO dto = new ChangeClosingInvoicePatchDTO("1000 1000 1000 1000", "123.456.789-10",  18);

        when(creditCardRepository.findByCardNumber(dto.cardNumber())).thenReturn(Optional.of(creditCard));

        assertThrows(InactiveCreditCardException.class, () -> creditCardPatchService.changeInvoiceClosing(dto));

        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }
}
