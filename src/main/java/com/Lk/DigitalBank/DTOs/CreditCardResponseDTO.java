package com.Lk.DigitalBank.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreditCardResponseDTO(
        Long id,
        LocalDate expirationDate,
        BigDecimal creditLimit,
        Long idAccount
) {
}
