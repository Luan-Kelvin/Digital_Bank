package com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchTest;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.UpdatePasswordDTO;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Exception.InactiveCreditCardException;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Exception.InvalidPasswordException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdatePasswordTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardPatchService creditCardPatchService;

    @Test
    @DisplayName("Deve alterar a senha do cartão")
    void alterarSenhaDoCartao(){
        Customer customer = new Customer("Luqinhas", "123.456.789-16", LocalDate.of(1998, 2, 2));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2458", "2545 8525 4545 5555" ,account, 12);
        UpdatePasswordDTO dto = new UpdatePasswordDTO("2545 8525 4545 5555", "123.456.789-16", "2458", "1818");

        when(creditCardRepository.findByCardNumber("2545 8525 4545 5555")).thenReturn(Optional.of(card));

        creditCardPatchService.updatePassword(dto);

        ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);

        verify(creditCardRepository).save(captor.capture());

        CreditCard cardSave = captor.getValue();

        assertEquals(dto.newPassword(), cardSave.getPassword());

        verify(creditCardRepository).findByCardNumber("2545 8525 4545 5555");
    }

    @Test
    @DisplayName("Deve lançar exceção se Cartão de crédito com número solicitado não existir")
    void LancaExcecaoQuandocartaoNaoExisteParaalteracaoDeSenha(){
        UpdatePasswordDTO dto = new UpdatePasswordDTO("2545 8525 4545 5555", "123.456.789-16", "2458", "1818");

        when(creditCardRepository.findByCardNumber(dto.cardNumber())).thenReturn(Optional.empty());

        assertThrows(CreditCardsNotExistException.class, () -> creditCardPatchService.updatePassword(dto));

        verify(creditCardRepository).findByCardNumber(dto.cardNumber());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se cartão de crédito estiver inativo pra uso.")
    void lancaExcecaoSeCartaoEstiverInativoparaAlterarSenha(){
        Customer customer = new Customer("Luqinhas", "123.456.789-16", LocalDate.of(1998, 2, 2));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2458", "2545 8525 4545 5555" ,account, 12);
        card.blockCard("2458");
        UpdatePasswordDTO dto = new UpdatePasswordDTO("2545 8525 4545 5555", "123.456.789-16", "2458", "1818");

        when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));

        assertThrows(InactiveCreditCardException.class, () -> creditCardPatchService.updatePassword(dto));

        verify(creditCardRepository).findByCardNumber(card.getCardNumber());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se cpf estiver diferente do solicitado.")
    void lancaExcecaoSeCpfEstiverIncorretoparaAlterarSenha(){
        Customer customer = new Customer("Luqinhas", "123.456.789-16", LocalDate.of(1998, 2, 2));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2458", "2545 8525 4545 5555" ,account, 12);
        UpdatePasswordDTO dto = new UpdatePasswordDTO("2545 8525 4545 5555", "223.456.789-16", "2458", "1818");

        when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));

        assertThrows(InvalidCPFException.class, () -> creditCardPatchService.updatePassword(dto));

        verify(creditCardRepository).findByCardNumber(card.getCardNumber());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se senha estiver diferente do cartão encontrado.")
    void lancaExcecaoSeSenhaEstiverIncorretaNaHoraDeAlterarSenha(){
        Customer customer = new Customer("Luqinhas", "123.456.789-16", LocalDate.of(1998, 2, 2));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard card = new CreditCard("2458", "2545 8525 4545 5555" ,account, 12);
        UpdatePasswordDTO dto = new UpdatePasswordDTO("2545 8525 4545 5555", "123.456.789-16", "5541", "1818");

        when(creditCardRepository.findByCardNumber(card.getCardNumber())).thenReturn(Optional.of(card));

        assertThrows(InvalidPasswordException.class, () -> creditCardPatchService.updatePassword(dto));

        verify(creditCardRepository).findByCardNumber(card.getCardNumber());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }
}
