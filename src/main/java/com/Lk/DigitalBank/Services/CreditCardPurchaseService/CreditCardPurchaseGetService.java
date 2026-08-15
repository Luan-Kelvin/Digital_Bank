package com.Lk.DigitalBank.Services.CreditCardPurchaseService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.CreditCradPurchase.CreditCardPurchaseGetDTO;
import com.Lk.DigitalBank.Entity.CreditCardPurchase;
import com.Lk.DigitalBank.Repository.CreditCardPurchseRespository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditCardPurchaseGetService {
    private final Logger logger = LoggerFactory.getLogger(CreditCardPurchaseGetService.class);
    private final Conversor conversor;
    private final CreditCardPurchseRespository creditCardPurchseRespository;

    // LISTAR CREDITCARDSPURCHASES
    public List<CreditCardPurchaseGetDTO> listCreditCardPurchase() {
        List<CreditCardPurchase> purchases = creditCardPurchseRespository.findAll();

        if (purchases.isEmpty()){
            logger.info("Nenhuma compra em cartão registrada ate o momento.");
            return List.of();
        }

        List<CreditCardPurchaseGetDTO> dto = new ArrayList<>();

        purchases.forEach(p -> {
            dto.add(conversor.converterCreditCardPurchase(p));
        });

        return dto;
    }
}
