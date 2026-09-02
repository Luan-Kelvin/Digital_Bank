package com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch;


public record CreditCardPatchBlockedDTO(
        String number,
        String cpf,
        String password
){
}
