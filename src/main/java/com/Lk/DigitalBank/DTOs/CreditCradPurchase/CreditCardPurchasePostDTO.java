package com.Lk.DigitalBank.DTOs.CreditCradPurchase;

import java.math.BigDecimal;

public record CreditCardPurchasePostDTO(
        String description,
        BigDecimal amountPurchase,
        Integer installments,
        String merchant,
        Long creditCardId
) {
}
