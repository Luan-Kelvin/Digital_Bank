package com.Lk.DigitalBank.Services.InvoiceService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Invoice.InvoiceGetDTO;
import com.Lk.DigitalBank.Entity.Invoice;
import com.Lk.DigitalBank.Repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceGetService {
    private final Logger logger = LoggerFactory.getLogger(InvoiceGetService.class);
    private final Conversor conversor;
    private final InvoiceRepository invoiceRepository;

    public List<InvoiceGetDTO> listInvoices(){
        List<Invoice> invoices = invoiceRepository.findAll();

        if (invoices.isEmpty()){
            logger.info("Erro! Nenhuma fatura localizada no banco.");
            return List.of();
        }

        List<InvoiceGetDTO> dto = invoices.stream()
                .map(conversor::converterInvoice)
                .toList();

        return dto;
    }
}
