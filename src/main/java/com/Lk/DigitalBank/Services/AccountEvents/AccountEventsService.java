package com.Lk.DigitalBank.Services.AccountEvents;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsGetDTO;
import com.Lk.DigitalBank.DTOs.AccountEvents.AccountEventsPostDTO;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.AccountEvents;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Services.AccountService.AccountGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountEventsService {

    private final AccountEventsRepository accountEventsRepository;

    private final AccountGetService accountService;

    private final Conversor conversor;

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

        account.addEvent(events);

        accountEventsRepository.save(events);

        return conversor.converterAccountevents(events);
    }


    // GERAR SEQUENCE NUMBER
    private Long generateSequenceNumber(Long idAccount){
        return accountEventsRepository.findLastSequenceNumber(idAccount) + 1;
    }
}
