package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    // BUSCAR POR NÚMERO DO CARTÃO
    Optional<CreditCard> findByCardNumber(String number);

    // BUSCAR POR DATA DE VENCIMENTO DO CARTÃO
    List<CreditCard> findByExpirationDateBetween(LocalDate min, LocalDate max);

    // BUSCAR POR STATUS DO CARTÃO
    List<CreditCard> findByCardStatus(CardStatus cardStatus);

    // BUSCAR POR ID DA CONTA
    Optional<CreditCard> findByAccountId(Long idAccount);

    // BUSCAR CARTÕES DE CRÉDITO ATIVOS
    @Query("""
            SELECT c
            FROM CreditCard c
            WHERE c.cardStatus = 'ACTIVE'
            """)
    List<CreditCard> searchCreditCardActive();

    // BUSCAR CARTÕES DE CRÉDITO BLOQUEADOS
    @Query("""
            SELECT c
            FROM CreditCard c
            WHERE c.cardStatus = 'BLOCKED'
            """)
    List<CreditCard> searchCreditCardBlocked();

    // BUSCAR CARTÕES DE CRÉDITO EXPIRADOS
    @Query("""
            SELECT c
            FROM CreditCard c
            WHERE c.cardStatus = 'EXPIRED'
            """)
    List<CreditCard> searchCreditCardExpired();

    // BUSCAR CARTÕES DE CRÉDITO CANCELADOS
    @Query("""
            SELECT c
            FROM CreditCard c
            WHERE c.cardStatus = 'CANCELED'
            """)
    List<CreditCard> searchCreditCardCanceled();



}
