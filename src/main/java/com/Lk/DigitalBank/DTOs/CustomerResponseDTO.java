package com.Lk.DigitalBank.DTOs;

import java.time.LocalDate;

public record CustomerResponseDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        Long AccountID
) {
}
