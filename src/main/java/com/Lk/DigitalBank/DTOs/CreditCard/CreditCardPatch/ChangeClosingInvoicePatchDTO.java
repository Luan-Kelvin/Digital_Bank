package com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ChangeClosingInvoicePatchDTO(
        String cardNumber,

        String cpfCustomer,

        @Max(value = 25, message = "Dia de fechamento deve ser no máximo ate 25.")
        @Min(value = 1, message = "Dia de fechamento deve ser no mímino 1")
        Integer newDay
) {
}
