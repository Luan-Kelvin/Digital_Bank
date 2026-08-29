package com.Lk.DigitalBank.DTOs.Account;

import java.math.BigDecimal;

public record TransferPixDTO(
        String senderAccount,
        String recipientAccount,
        BigDecimal value
) {
}
