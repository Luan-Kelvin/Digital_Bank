package com.Lk.DigitalBank.DTOs.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record InvoiceGetDTO(
        Long id,
        YearMonth referenceMonth,
        LocalDate closingDate,
        LocalDate dueDate,
        BigDecimal totalAmount,
        Long creditCardID
) {
}
