package com.Lk.DigitalBank.DTOs.AccountEvents;

import com.Lk.DigitalBank.ENUM.EventType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountEventsPostDTO(

        @NotNull
        Long id_account,

        @NotNull
        EventType type,

        @NotNull
        @Positive
        BigDecimal amount
) {
}
