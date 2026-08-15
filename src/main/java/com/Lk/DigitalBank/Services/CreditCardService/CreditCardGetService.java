package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Repository.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardGetService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardGetService.class);
    private final Conversor conversor;
    private final CreditCardRepository creditCardRepository;

    // LISTAR CREDIT CARD
    public List<CreditCardGetDTO> listCreditCards(){
        List<CreditCard> cards = creditCardRepository.findAll();

        if (cards.isEmpty()){
            logger.info("Não foi localizado nenhum cartão de crédito cadastrado no banco.");
            return List.of();
        }

        List<CreditCardGetDTO> dto = new ArrayList<>();

        cards.forEach(c -> {
            dto.add(conversor.converterCreditCard(c));
        });

        return dto;
    }
}
