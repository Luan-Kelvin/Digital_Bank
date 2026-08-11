package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // BUSCAR POR NÚMERO DA CONTA
    Optional<Account> findByAccountNumber(String number);

    // BUSCAR POR TIPO DE CONTA
    List<Account> findByAccountType(AccountType type);

    // BUSCAR POR STATUS DA CONTA
    List<Account> findByAccountStatus(AccountStatus status);

    // BUSCANDO POR PERIODO DE CRIAÇÃO
    List<Account> findByCreationDateBetween(LocalDate min, LocalDate max);

    // BUSCANDO POR ID DE CUSTOMER
    List<Account> findByCustomerId(Long idCustomer);

    // BUSCANDO POR NOME DO CUSTOMER
    List<Account> findByCustomerNameIgnoreCase(String name);

    // BUSCANDO POR CPF DE CUSTOMER
    List<Account> findByCustomerCpf(String cpf);

    //BUSCAR POR ID DE CARTÃO DE CRÉDITO
    Optional<Account> findByCreditCardId(Long id);

    // BUSCAR POR NÚMERO DO CARTÃO DE CRÉDITO
    Optional<Account> findByCardNumber(String cardNumber);

    @Query(value = """
            SELECT nextval('entitys.account_number_seq')
            """, nativeQuery = true)
    Long nextAccountNumber();
}
