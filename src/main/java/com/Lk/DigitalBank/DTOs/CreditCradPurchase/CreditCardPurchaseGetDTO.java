package com.Lk.DigitalBank.DTOs.CreditCradPurchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditCardPurchaseGetDTO(
        Long id,
        String description,
        BigDecimal amountPurchase,
        LocalDateTime purchaseDate,
        Integer installments,
        BigDecimal installmentAmount,
        String merchant,
        Long creditCardId
) {
}
