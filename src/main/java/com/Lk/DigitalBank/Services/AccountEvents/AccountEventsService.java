package com.Lk.DigitalBank.Services.AccountEvents;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsGetDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsPostDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.AccountEvents;
import com.Lk.DigitalBank.Entity.AccountSnapshots;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Repository.AccountSnapshotsRepository;
import com.Lk.DigitalBank.Services.AccountService.AccountGetService;
import com.Lk.DigitalBank.Services.AccountSnapshots.AccountSnapshotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountEventsService {

    private final AccountEventsRepository accountEventsRepository;

    private final AccountSnapshotsRepository accountSnapshotsRepository;

    private final AccountGetService accountService;

    private final Conversor conversor;

    private final AccountSnapshotsService accountSnapshotsService;


    // REGISTRAR UM NOVO EVENTO
    @Transactional
    public AccountEventsGetDTO registerEvents(AccountEventsPostDTO postDTO){
        Account account = accountService.findById(postDTO.id_account());

        Long sequenceNumber = generateSequenceNumber(account.getId());

        AccountEvents events = new AccountEvents(
                account,
                postDTO.type(),
                postDTO.amount(),
                sequenceNumber
        );

        account.applyEvent(events);

        accountEventsRepository.save(events);

        account.addEvent(events);

        Optional<AccountSnapshots> snapshots = accountSnapshotsRepository.findByAccount_Id(account.getId());

        if (!snapshots.isPresent()){

            Long totalEvents = accountEventsRepository.countByAccount_Id(account.getId());

            if (totalEvents >= 10){
                accountSnapshotsService.createSnapshots(account.getId());
            }
        } else {
            Long eventsSinceSnapshot = accountEventsRepository.countByAccount_IdAndSequenceNumberGreaterThan(
                    account.getId(),
                    snapshots.get().getLastSequenceNumber()
            );

            if (eventsSinceSnapshot >= 10) {
                accountSnapshotsService.createSnapshots(account.getId());
            }
        }

        return conversor.converterAccountevents(events);
    }


    // GERAR SEQUENCE NUMBER
    private Long generateSequenceNumber(Long idAccount){
        return accountEventsRepository.findLastSequenceNumber(idAccount) + 1;
    }
}
