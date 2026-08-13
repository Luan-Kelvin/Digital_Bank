package com.Lk.DigitalBank.DTOs.Account;

import com.Lk.DigitalBank.ENUM.AccountType;

public record AccountPostDTO(
        String cpfCustomer,
        AccountType accountType
) {
}
