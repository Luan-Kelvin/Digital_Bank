package com.Lk.DigitalBank.Services.AccountEvents;

import com.Lk.DigitalBank.Entity.AccountEvents;
import com.Lk.DigitalBank.Entity.AccountSnapshots;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Repository.AccountSnapshotsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBalanceReconstructor {

    private final AccountEventsRepository accountEventsRepository;
    private final AccountSnapshotsRepository accountSnapshotsRepository;

    @Transactional(readOnly = true)
    public BigDecimal reconstructBalance(Long idAccount){
        AccountSnapshots accountSnapshots = accountSnapshotsRepository.findByAccount_Id(idAccount)
                .orElseThrow(() -> new IllegalArgumentException("Snapshot não encontrado para a conta: " + idAccount));


        List<AccountEvents> events = accountEventsRepository
                .findAllByAccount_IdAndSequenceNumberGreaterThanOrderBySequenceNumberAsc(
                idAccount, accountSnapshots.getLastSequenceNumber());

        BigDecimal balance = accountSnapshots.getBalance();

        for (AccountEvents event : events){

            switch (event.getEventType()){
                case MONEY_DEPOSITED, PIX_RECEIVED -> balance = balance.add(event.getAmount());
                case MONEY_WITHDRAWN, PIX_SENT -> balance = balance.subtract(event.getAmount());
                default -> throw new IllegalArgumentException(
                        "Evento ainda não implementado na reconstrução: "
                                + event.getEventType()
                );
            }
        }

        return  balance;

    }

    @Transactional(readOnly = true)
    public BigDecimal reconstructBalanceViaEvents(Long idAccoubnt){
        List<AccountEvents> events = accountEventsRepository.findAllByAccount_IdOrderBySequenceNumberAsc(idAccoubnt);

        BigDecimal balance = BigDecimal.ZERO;

        for (AccountEvents event: events){
            switch (event.getEventType()){
                case MONEY_DEPOSITED, PIX_RECEIVED, ACCOUNT_CREATED -> balance = balance.add(event.getAmount());
                case MONEY_WITHDRAWN, PIX_SENT -> balance = balance.subtract(event.getAmount());
                default -> throw new IllegalArgumentException(
                        "Evento ainda não implementado na reconstrução: "
                                + event.getEventType()
                );
            }
        }

        return balance;
    }
}
