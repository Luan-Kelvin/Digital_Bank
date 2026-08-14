package com.Lk.DigitalBank.DTOs.Customer;

import com.Lk.DigitalBank.Entity.Account;

import java.time.LocalDate;
import java.util.List;

public record CustomerGetDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<Account> Accounts
) {
}
