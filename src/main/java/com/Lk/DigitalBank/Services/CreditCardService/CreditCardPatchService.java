package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Exception.CreditCardDoesNotBlockedException;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Exception.InvalidPasswordException;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditCardPatchService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardPatchService.class);
    private final CreditCardRepository creditCardRepository;

    // BLOQUEAR CARTÃO DE CRÉDITO
    @Transactional
    public void blockCard(CreditCardPatchBlockedDTO dto){
        CreditCard card = creditCardRepository.findByCardNumber(dto.number())
                .orElseThrow(() -> new CreditCardsNotExistException(String.format("ERRO! Cartão com Nº não foi encontrado.", dto.number())));

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
}
