package com.Lk.DigitalBank.DTOs.AccountEvents;

import com.Lk.DigitalBank.ENUM.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountEventsGetDTO(
        Long id,
        Long id_account,
        Long sequence,
        EventType type,
        BigDecimal amount,
        LocalDateTime occurredAt
) {
}
