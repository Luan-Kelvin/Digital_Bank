package com.Lk.DigitalBank.Services.AccountEvents;

import com.Lk.DigitalBank.Entity.AccountEvents;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBalanceReconstructor {

    private final AccountEventsRepository accountEventsRepository;
    private final AccountEventsService accountEventsService;

    @Transactional
    public BigDecimal reconstructBalance(Long accounId){

        List<AccountEvents> events = accountEventsRepository.findAllByAccount_IdOrderBySequenceNumberAsc(accounId);

        BigDecimal balance = BigDecimal.ZERO;

        for (AccountEvents event: events){
            switch (event.getEventType()){
                case MONEY_DEPOSITED -> balance = balance.add(event.getAmount());
                case MONEY_WITHDRAWN -> balance = balance.subtract(event.getAmount());
                default -> throw new IllegalArgumentException(
                        "Evento ainda não implementado na reconstrução: "
                                + event.getEventType()
                );
            }
        }

        return balance;
    }
}
