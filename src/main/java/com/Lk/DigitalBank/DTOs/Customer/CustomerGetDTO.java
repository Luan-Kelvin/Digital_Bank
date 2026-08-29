package com.Lk.DigitalBank.DTOs.Customer;

import com.Lk.DigitalBank.ENUM.CustomerStatus;

import java.time.LocalDate;
import java.util.List;

public record CustomerGetDTO(
        Long id,
        String name,
        LocalDate dateOfBirth,
        CustomerStatus status,
        List<String> Accounts
) {
}
