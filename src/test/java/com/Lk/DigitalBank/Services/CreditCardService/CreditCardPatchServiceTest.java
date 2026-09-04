package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.UpdatePasswordDTO;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.*;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
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
public class CreditCardPatchServiceTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardPatchService creditCardPatchService;

    @Test
    @DisplayName("Deve verificar se status de conta do cartão muda quando para blocked")
    void mudandoStatusParaBlocked(){
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
    @DisplayName("Deve lançar exceção se CPF for diferente")
    void lancaExcecaoSeCpfForDiferente(){
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
    @DisplayName("Deve lançar exceção se senha for diferente")
    void lancaExcecaoSeSenhaForDiferente(){
        Customer customer = new Customer("Jandre", "123.456.789-10", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("4555", "5412 2587 7897 5454", account, 25);

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO("5412 2587 7897 5454", "123.456.789-10", "4325");

        when(creditCardRepository.findByCardNumber(dto.number())).thenReturn(Optional.of(creditCard));

        assertThrows(InvalidPasswordException.class, () -> creditCardPatchService.blockCard(dto));

        verify(creditCardRepository).findByCardNumber(dto.number());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve mudar status do card para ctive")
    void mudarStatusParaCAtive(){
        Customer customer = new Customer("Jandre", "123.456.789-10", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        String number = "1234 5678 9101 1213";
        CreditCard creditCard = new CreditCard("4555", number, account, 25);
        creditCard.blockCard("4555");

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO(number, "123.456.789-10", "4555");

        when(creditCardRepository.findByCardNumber(number)).thenReturn(Optional.of(creditCard));

        creditCardPatchService.unlockCard(dto);

        assertEquals(CardStatus.ACTIVE, creditCard.getCardStatus());

        verify(creditCardRepository).findByCardNumber(number);
        verify(creditCardRepository).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se cartão estiver ativo")
    void lncaExcecaoSeCartaoEstiverAtivo(){
        Customer customer = new Customer("Jandre", "545.789.888-25", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("4555", "5412 2587 7897 5454", account, 25);

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO("5412 2587 7897 5454", "123.456.789-10", "4555");

        when(creditCardRepository.findByCardNumber(creditCard.getCardNumber())).thenReturn(Optional.of(creditCard));

        assertThrows(CreditCardDoesNotBlockedException.class, () -> creditCardPatchService.unlockCard(dto));

        verify(creditCardRepository).findByCardNumber(dto.number());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se CPF for diferente no desbloqueio")
    void lancaExcecaoSeCpfForDiferenteNaHoraDeDesbloquear(){
        Customer customer = new Customer("Jandre", "545.789.888-25", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("4555", "5412 2587 7897 5454", account, 25);
        creditCard.blockCard("4555");

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO("5412 2587 7897 5454", "123.456.789-10", "4555");

        when(creditCardRepository.findByCardNumber(dto.number())).thenReturn(Optional.of(creditCard));

        assertThrows(InvalidCPFException.class, () -> creditCardPatchService.unlockCard(dto));

        verify(creditCardRepository).findByCardNumber(dto.number());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se senha for diferente no desbloqueio.")
    void lancaExcecaoSeSenhaForDiferenteNoDesbloqueio(){
        Customer customer = new Customer("Jandre", "123.456.789-10", LocalDate.of(1999, 2, 12));
        Account account = new Account(customer, AccountType.CURRENT);
        CreditCard creditCard = new CreditCard("4555", "5412 2587 7897 5454", account, 25);
        creditCard.blockCard("4555");

        CreditCardPatchBlockedDTO dto = new CreditCardPatchBlockedDTO("5412 2587 7897 5454", "123.456.789-10", "4325");

        when(creditCardRepository.findByCardNumber(dto.number())).thenReturn(Optional.of(creditCard));

        assertThrows(InvalidPasswordException.class, () -> creditCardPatchService.unlockCard(dto));

        verify(creditCardRepository).findByCardNumber(dto.number());
        verify(creditCardRepository, never()).save(any(CreditCard.class));
    }

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
    @DisplayName("Deve lançar exceção se cartão de crédito estiver inativo pra uso.")
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
    @DisplayName("Deve lançar exceção se cartão de crédito estiver inativo pra uso.")
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
