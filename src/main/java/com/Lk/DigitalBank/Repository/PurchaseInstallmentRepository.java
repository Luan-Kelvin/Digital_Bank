package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.PurchaseInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseInstallmentRepository extends JpaRepository<PurchaseInstallment, Long> {

    // BUSCAR POR ID FATURA
    List<PurchaseInstallment> findByInvoiceId(Long id);
}
