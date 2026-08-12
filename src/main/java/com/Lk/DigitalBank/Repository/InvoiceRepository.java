package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    // BUSCAR POR DATA DE FECHAMENTO DE FATURA
    List<Invoice> findByClosingDate(LocalDate date);

    // BUSCAR POR DATA DE VENCIMENTO
    List<Invoice> findByDueDate(LocalDate date);

    // BUSCAR POR ID DO CARTÃO
    List<Invoice> findByCreditCardId(Long id);

    //
}
