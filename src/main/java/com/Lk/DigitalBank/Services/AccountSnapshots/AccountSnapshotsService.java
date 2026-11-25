package com.Lk.DigitalBank.Services.AccountSnapshots;

import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.AccountSnapshots;
import com.Lk.DigitalBank.Exception.AccountDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountEventsRepository;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.AccountSnapshotsRepository;
import com.Lk.DigitalBank.Services.AccountEvents.AccountBalanceReconstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountSnapshotsService {

    private final AccountSnapshotsRepository accountSnapshotsRepository;
    private final AccountEventsRepository accountEventsRepository;
    private final AccountRepository accountRepository;
    private final AccountBalanceReconstructor accountBalanceReconstructor;


    @Transactional
    public void createSnapshots(Long idAccount){
            Account account = accountRepository.findByIdForUpdate(idAccount)
                    .orElseThrow(() -> new AccountDoesNotExistException("ERRO! Conta não foi encontrada."));

            Long lastNumber = accountEventsRepository.findLastSequenceNumber(account.getId());

            var existingSnapshot = accountSnapshotsRepository
                    .findByAccount_Id(account.getId());

            if (existingSnapshot.isEmpty()){

                BigDecimal balance = accountBalanceReconstructor.reconstructBalanceViaEvents(account.getId());

                AccountSnapshots accountSnapshots = new AccountSnapshots(
                        account, balance, lastNumber
                );

                account.addSnapshots(accountSnapshots);

                accountSnapshotsRepository.save(accountSnapshots);
            } else {
                BigDecimal balance = accountBalanceReconstructor.reconstructBalance (account.getId());

                existingSnapshot.get().updateSnapshot(balance, lastNumber);
            }

        }

    }
