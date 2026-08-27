package com.Lk.DigitalBank.DTOs.Customer;

import java.time.LocalDate;

public record CustomerPatchDTO (
        Long id,
        String nome,
        LocalDate dateOfBirth
){
}
