package com.Lk.DigitalBank.Components;

import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {
    private final AccountRepository accountRepository;

    public String generate(){
        Long num = accountRepository.nextAccountNumber();

        return num + "-1";
    }
}
