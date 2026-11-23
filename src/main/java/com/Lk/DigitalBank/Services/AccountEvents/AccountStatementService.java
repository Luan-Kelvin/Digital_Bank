package com.Lk.DigitalBank.Services.AccountEvents;

import com.Lk.DigitalBank.DTOs.AccountEvents.AccountStatementDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountStatementEntryDTO;
import com.Lk.DigitalBank.Entity.AccountEvents;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountStatementService {

    private final AccountEventsRepository accountEventsRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public AccountStatementDTO generateStatement(Long idAccount){

        if (!accountRepository.existsById(idAccount)){
            throw new AccountDoesNotExistException(String.format("ERRO! Conta com ID %s, não existe.", idAccount));
        }

        List<AccountEvents> events = accountEventsRepository.findAllByAccount_IdOrderBySequenceNumberAsc(idAccount);

        BigDecimal balance = BigDecimal.ZERO;

        List<AccountStatementEntryDTO> statements = new ArrayList<>();

        for (AccountEvents event : events) {

            switch (event.getEventType()) {

                case MONEY_DEPOSITED, PIX_RECEIVED -> balance = balance.add(event.getAmount());

                case MONEY_WITHDRAWN, PIX_SENT -> balance = balance.subtract(event.getAmount());

                default -> throw new IllegalArgumentException(
                        "Evento ainda não implementado no extrato: "
                                + event.getEventType()
                );
            }

            AccountStatementEntryDTO entry =
                    new AccountStatementEntryDTO(
                            event.getSequenceNumber(),
                            event.getEventType(),
                            event.getAmount(),
                            event.getOccurredAt(),
                            balance
                    );

            statements.add(entry);
        }

        return new AccountStatementDTO(
                idAccount,
                balance,
                statements
        );

    }
}
