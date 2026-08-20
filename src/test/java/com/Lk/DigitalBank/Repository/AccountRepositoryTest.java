package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void deveBuscarAccountPorNumeroDeConta(){
        Account account = new Account();

        account.addNumberAccount("12345");

        accountRepository.save(account);

        Optional<Account> ac = accountRepository.findByAccountNumber("12345");

        assertTrue(ac.isPresent());

        assertEquals("12345", ac.get().getAccountNumber());
    }
}
