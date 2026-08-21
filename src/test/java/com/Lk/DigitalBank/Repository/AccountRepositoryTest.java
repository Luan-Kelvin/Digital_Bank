package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
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

    @Test
    public void deveBuscarTodasAsContasDoBanco(){
        Account account1 = new Account();
        Account account2 = new Account();

        account1.addNumberAccount("12345");
        account2.addNumberAccount("54321");

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findAll();

        assertEquals(2, accounts.size());
        assertEquals("12345", accounts.get(0).getAccountNumber());
        assertEquals("54321", accounts.get(1).getAccountNumber());
    }
}
