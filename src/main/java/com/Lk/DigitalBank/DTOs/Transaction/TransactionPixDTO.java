package com.Lk.DigitalBank.DTOs.Transaction;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionPixDTO(
        LocalDateTime dateAndTime,
        BigDecimal value,
        String description,
        String accountNumberSender,
        String accountNumberRecipient

) {
}
