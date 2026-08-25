package com.Lk.DigitalBank.DTOs.Customer;

import java.time.LocalDate;
import java.util.List;

public record CustomerGetDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        List<String> Accounts
) {
}
