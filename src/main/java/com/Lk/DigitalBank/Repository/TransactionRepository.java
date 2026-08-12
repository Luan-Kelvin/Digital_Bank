package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.ENUM.TransactionType;
import com.Lk.DigitalBank.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // BUSCAR POR TIPO
    List<Transaction> findByTransactionType(TransactionType type);

    // BUSCAR POR VALOR
    List<Transaction> findByTransactionValue(BigDecimal value);

    // BUSCAR POR ID DA CONTA
    List<Transaction> findByAccountId(Long id);
}
