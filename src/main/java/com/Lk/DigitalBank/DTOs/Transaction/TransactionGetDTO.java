package com.Lk.DigitalBank.DTOs.Transaction;

import com.Lk.DigitalBank.ENUM.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionGetDTO(
        Long id,
        TransactionType type,
        BigDecimal value,
        LocalDateTime dateAndTime,
        String description,
        String accountNumber
) {
}
