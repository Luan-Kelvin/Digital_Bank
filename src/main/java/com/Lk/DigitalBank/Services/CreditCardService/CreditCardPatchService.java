package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchLimitDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.UpdatePasswordDTO;
import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Exception.*;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditCardPatchService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardPatchService.class);
    private final Conversor conversor;
    private final CreditCardRepository creditCardRepository;

    // BLOQUEAR CARTÃO DE CRÉDITO
    @Transactional
    public void blockCard(CreditCardPatchBlockedDTO dto){
        CreditCard card = creditCardRepository.findByCardNumber(dto.number())
                .orElseThrow(() -> new CreditCardsNotExistException(String.format("ERRO! Cartão com Nº não foi encontrado.", dto.number())));

        if (card.getCardStatus() != CardStatus.ACTIVE){
            throw new InactiveCreditCardException("ERRO! Cartão de crédito está inativo.");
        }

        if (!dto.cpf().equals(card.getAccount().getCustomer().getCpf())){
            throw new InvalidCPFException("ERRO! Cpf diferente do titular da conta.");
        }

        if (!dto.password().equals(card.getPassword())){
            throw new InvalidPasswordException("ERRO! senha incorreta.");
        }

        card.blockCard(dto.password());
        creditCardRepository.save(card);
        logger.info(String.format("Cartão Nº%s bloqueado com sucesso!", dto.number()));
    }

    // DESBLOQUEAR CARTÃO
    @Transactional
    public void unlockCard(CreditCardPatchBlockedDTO dto){
        CreditCard card = creditCardRepository.findByCardNumber(dto.number())
                .orElseThrow(() -> new CreditCardsNotExistException("ERRO! Cartão de crédito não existe no banco."));

        if (card.getCardStatus() != CardStatus.BLOCKED){
            throw new CreditCardDoesNotBlockedException("ERRO! Cartão de crédito NÃO esta bloqueado.");
        }

        if (!dto.cpf().equals(card.getAccount().getCustomer().getCpf())){
            throw new InvalidCPFException("ERRO! Cpf diferente do titular da conta.");
        }

        if (!dto.password().equals(card.getPassword())){
            throw new InvalidPasswordException("ERRO! senha incorreta.");
        }

        card.unlockCard(dto.password());
        creditCardRepository.save(card);
        logger.info(String.format("Cartão Nº%s desbloqueado com sucesso!", dto.number()));

    }

    // ALTERAR SENHA DO CARTÃO
    @Transactional
    public void updatePassword(UpdatePasswordDTO dto){
        CreditCard card = creditCardRepository.findByCardNumber(dto.cardNumber())
                .orElseThrow(() ->  new CreditCardsNotExistException(String.format("ERO! cartão de crédito com Nº n%s não existe.", dto.cardNumber())));

        if (card.getCardStatus() != CardStatus.ACTIVE){
            throw new InactiveCreditCardException("ERRO! Cartão de crédito está inativo.");
        }

        if (!dto.cpf().equals(card.getAccount().getCustomer().getCpf())){
            throw new InvalidCPFException("ERRO! Cpf diferente do titular da conta.");
        }

        if (!dto.oldPassword().equals(card.getPassword())){
            throw new InvalidPasswordException("ERRO! senha incorreta.");
        }

        card.changePassword(dto.newPassword());
        creditCardRepository.save(card);
        logger.info(String.format("Senha do cartão Nº %s alterada com sucesso!", dto.cardNumber()));

    }

    // AUMENTAR LIMITE
    @Transactional
    public CreditCardGetDTO increaseLimit(CreditCardPatchLimitDTO dto){
        CreditCard card = creditCardRepository.findByCardNumber(dto.cardNumber())
                .orElseThrow(() -> new CreditCardsNotExistException(String.format("Cartão com Nº %s não existe.", dto.cardNumber())));

        if (card.getCardStatus() != CardStatus.ACTIVE){
            throw new InactiveCreditCardException("ERRO! Cartão de crédito esta inativo.");
        }

        BigDecimal oldLimit = card.getCreditLimit();

        card.increaseLimit(dto.valueIncrease());
        creditCardRepository.save(card);
        logger.info(String.format("Limite do cartão Nº %s aumentado, ANTES: R$%s, AGORA: R$%s", dto.cardNumber(), oldLimit, card.getCreditLimit()));

        return conversor.converterCreditCard(card);
    }

    // REDUZIR LIMITE
    public CreditCardGetDTO reduceLimit(CreditCardPatchLimitDTO dto){
        CreditCard card = creditCardRepository.findByCardNumber(dto.cardNumber())
                .orElseThrow(() -> new CreditCardsNotExistException(String.format("ERRO! cartão com Nº %s não existe.", dto.cardNumber())));

        if (card.getCardStatus() != CardStatus.ACTIVE){
            throw new InactiveCreditCardException("ERRO! Cartão de crédito esta inativo.");
        }

        BigDecimal oldLimit = card.getCreditLimit();

        card.lowerLimit(dto.valueIncrease());
        creditCardRepository.save(card);
        logger.info(String.format("Limite do cartão Nº %s reduzido com sucessor, ANTES: R$ %s | DEPOIS: RS %s", dto.cardNumber(), oldLimit, card.getCreditLimit()));

        return conversor.converterCreditCard(card);
    }


}
