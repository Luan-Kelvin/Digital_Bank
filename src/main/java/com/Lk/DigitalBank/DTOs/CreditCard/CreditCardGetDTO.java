package com.Lk.DigitalBank.DTOs.CreditCard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardGetDTO(
        Long id,
        LocalDate expirationDate,
        BigDecimal creditLimit,
        Long idAccount
) {
}
