package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Exception.InvalidPasswordException;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
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
}
