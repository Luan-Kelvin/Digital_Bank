package com.Lk.DigitalBank.DTOs;

import com.Lk.DigitalBank.ENUM.AccountType;

import java.math.BigDecimal;

public record AccountResponseDTO(
        String accountNumber,
        BigDecimal balance,
        AccountType accountType,
        AccountType accountStatus,
        Long customerId,
        String customerName
) {
}
