package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void deveBuscarAccountPorNumeroDeConta(){
        Account account = new Account();

        account.addNumberAccount("1010 1010 1010 1010");

        accountRepository.save(account);

        Optional<Account> ac = accountRepository.findByAccountNumber("1010 1010 1010 1010");

        assertTrue(ac.isPresent());

        assertEquals("1010 1010 1010 1010", ac.get().getAccountNumber());
    }

    @Test
    public void deveBuscarTodasAsContasDoBanco(){
        Account account1 = new Account();
        Account account2 = new Account();

        account1.addNumberAccount("1010 1010 1010 1010");
        account2.addNumberAccount("2020 2020 2020 2020");

        accountRepository.save(account1);
        accountRepository.save(account2);

        List<Account> accounts = accountRepository.findAll();

        assertEquals(2, accounts.size());
        assertEquals("1010 1010 1010 1010", accounts.get(0).getAccountNumber());
        assertEquals("2020 2020 2020 2020", accounts.get(1).getAccountNumber());
    }
}
