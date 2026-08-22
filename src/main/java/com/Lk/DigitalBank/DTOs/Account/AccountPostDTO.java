package com.Lk.DigitalBank.DTOs.Account;

import com.Lk.DigitalBank.ENUM.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AccountPostDTO(

        @NotBlank
        @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")
        String cpfCustomer,

        @NotNull
        AccountType accountType
) {
}
