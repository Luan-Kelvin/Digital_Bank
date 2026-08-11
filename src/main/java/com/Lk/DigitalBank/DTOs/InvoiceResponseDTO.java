package com.Lk.DigitalBank.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record InvoiceResponseDTO(
        Long id,
        YearMonth referenceMonth,
        LocalDate closingDate,
        LocalDate dueDate,
        BigDecimal totalAmount,
        Long creditCardID
) {
}
