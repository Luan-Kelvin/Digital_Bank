package com.Lk.DigitalBank.DTOs.Account;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;

import java.math.BigDecimal;

public record AccountGetDTO(
        String accountNumber,
        BigDecimal balance,
        AccountType accountType,
        AccountStatus accountStatus,
        Long customerId,
        String customerName
) {
}
