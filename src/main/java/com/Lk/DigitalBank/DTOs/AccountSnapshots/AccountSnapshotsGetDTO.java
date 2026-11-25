package com.Lk.DigitalBank.DTOs.AccountSnapshots;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountSnapshotsGetDTO(
        Long id,
        Long id_account,
        BigDecimal balance,
        Long lastSequenceNumber,
        LocalDateTime createdAt
) {
}
