package com.Lk.DigitalBank.Services.CreditCardService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Exception.CreditCardsNotExistException;
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

    // LISTAR CREDIT CARD ATIVOS
    public List<CreditCardGetDTO> listCreditCards(){
        List<CreditCard> cards = creditCardRepository.searchCreditCardActive();

        if (cards.isEmpty()){
            logger.info("Não foi localizado nenhum cartão de crédito  ativo no banco.");
            return List.of();
        }

        return converterLista(cards);
    }

    // LISTAR CREDIT CARD BLOQUEADOS
    public List<CreditCardGetDTO> listCreditCardsBlockeds(){
        List<CreditCard> cards = creditCardRepository.searchCreditCardBlocked();

        if (cards.isEmpty()){
            logger.info("Não foi localizado nenhum cartão de crédito Bloqueado no banco.");
            return List.of();
        }

        return converterLista(cards);
    }

    // LISTAR CREDIT CARD EXPIRED
    public List<CreditCardGetDTO> listCreditCardExpired(){
        List<CreditCard> cards = creditCardRepository.searchCreditCardExpired();

        if (cards.isEmpty()){
            logger.info("Não foi localizado nenhum cartão de crédito expirado no banco.");
            return List.of();
        }

        return converterLista(cards);

    }

    // LISTAR CREDIT CARD CANCELED
    public List<CreditCardGetDTO> listCreditCardCanceled(){
        List<CreditCard> cards = creditCardRepository.searchCreditCardCanceled();

        if (cards.isEmpty()){
            logger.info("Não foi localizado nenhum cartão de crédito cancelado no banco.");
            return List.of();
        }

        return converterLista(cards);
    }

    // BUSCAR CREDIT CARD POR NÚMERO
    public CreditCardGetDTO findByCardNumber(String number){
        CreditCard card = creditCardRepository.findByCardNumber(number)
                .orElseThrow(() -> new CreditCardsNotExistException(String.format("ERRO! Cart~o com número %s não existe no banco.", number)));

        return conversor.converterCreditCard(card);
    }


    // Cnverter Listas de CreditCard em dtos
    private List<CreditCardGetDTO> converterLista(List<CreditCard> list){
        List<CreditCardGetDTO> dto = new ArrayList<>();

        list.forEach(c -> {
            dto.add(conversor.converterCreditCard(c));
        });

        return dto;
    }
}
