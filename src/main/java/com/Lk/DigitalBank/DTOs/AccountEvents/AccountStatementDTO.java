package com.Lk.DigitalBank.DTOs.AccountEvents;

import java.math.BigDecimal;
import java.util.List;

public record AccountStatementDTO(
        Long accountId,
        BigDecimal finalBalance,
        List<AccountStatementEntryDTO> entries
) {
}
