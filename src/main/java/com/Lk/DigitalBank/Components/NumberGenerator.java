package com.Lk.DigitalBank.Components;

import com.Lk.DigitalBank.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class NumberGenerator {
    private final Random random = new Random();
    private final AccountRepository accountRepository;

    public String generateNumberAccount(){
        Long num = accountRepository.nextAccountNumber();

        return num + "-1";
    }

    public String generateNumberCard(){
        StringBuilder number = new StringBuilder();

        for (int i = 0;i < 16;i++){
            number.append(random.nextInt(10));

            if ((i + 1 ) % 4 == 0 && i != 15){
                number.append(" ");
            }
        }

        return number.toString();
    }
}
