package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {


}
