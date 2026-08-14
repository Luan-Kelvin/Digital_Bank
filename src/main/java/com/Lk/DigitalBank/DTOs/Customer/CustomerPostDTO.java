package com.Lk.DigitalBank.DTOs.Customer;

import java.time.LocalDate;

public record CustomerPostDTO(
        String name,
        String cpf,
        LocalDate dateOfBirth
) {
}
