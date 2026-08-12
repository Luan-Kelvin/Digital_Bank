package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.CreditCardPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CreditCardPurchseRespository extends JpaRepository<CreditCardPurchase, Long> {

    //BUSCAR POR VALOR TOTAL DA COMPRA
    List<CreditCardPurchase> findByAmountPurchaseBetween(BigDecimal min, BigDecimal max);

    // BUSCAR POR DATA DE COMPRA
    List<CreditCardPurchase> findByPurchaseDate(LocalDate min, LocalDate max);

    // BUSCAR POR NOME DO COMÉRCIO
    List<CreditCardPurchase> findByMerchantIgnoreCase(String merchantName);

    // BUSCAR POR NÚMERO DE CARTÃO
    List<CreditCardPurchase> findByCreditCardCardNumber(String cardNumber);


}
