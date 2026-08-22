package com.Lk.DigitalBank.DTOs.Account;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositAndWithDrawAccountDTO(
        @NotNull
        String accountNumber,

        @NotNull
        BigDecimal value
) {}
