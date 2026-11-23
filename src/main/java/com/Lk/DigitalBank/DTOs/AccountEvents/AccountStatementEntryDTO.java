package com.Lk.DigitalBank.DTOs.AccountEvents;

import com.Lk.DigitalBank.ENUM.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountStatementEntryDTO(
        Long sequence,
        EventType type,
        BigDecimal amount,
        LocalDateTime occurredAt,
        BigDecimal balanceAfter
) {
}
