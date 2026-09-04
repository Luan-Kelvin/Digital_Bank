package com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch;

import java.math.BigDecimal;

public record CreditCardPatchLimitDTO(
        String cardNumber,
        String cpfCustomer,
        BigDecimal valueIncrease
) {
}
