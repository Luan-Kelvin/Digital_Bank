package com.Lk.DigitalBank.DTOs.Customer;

import java.time.LocalDate;

public record CustomerGetDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        Long AccountID
) {
}
