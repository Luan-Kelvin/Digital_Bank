package com.Lk.DigitalBank.DTOs.Account;

import com.Lk.DigitalBank.ENUM.AccountType;

public record AccountPatchDTO(
        String accountNumber,
        AccountType type
) {
}
