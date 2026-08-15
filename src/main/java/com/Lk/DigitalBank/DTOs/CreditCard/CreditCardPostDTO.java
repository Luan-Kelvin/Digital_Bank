package com.Lk.DigitalBank.DTOs.CreditCard;

public record CreditCardPostDTO(
        String password,
        Long idAccount,
        Integer closingDayInvoice
) {
}
