package com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch;

public record UpdatePasswordDTO(
        String cardNumber,
        String cpf,
        String oldPassword,
        String newPassword
) {
}
