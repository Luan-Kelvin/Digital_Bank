package com.Lk.DigitalBank.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditCardPurchaseResponseDTO(
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
