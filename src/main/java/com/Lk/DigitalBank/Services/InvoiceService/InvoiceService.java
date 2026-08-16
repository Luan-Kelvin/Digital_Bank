package com.Lk.DigitalBank.Services.InvoiceService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Invoice.InvoiceGetDTO;
import com.Lk.DigitalBank.Entity.CreditCard;
import com.Lk.DigitalBank.Entity.Invoice;
import com.Lk.DigitalBank.Repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final Logger logger = LoggerFactory.getLogger(InvoiceService.class);
    private final Conversor conversor;
    private final InvoiceRepository invoiceRepository;

    // LISTAR TODAS AS FATURAS
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

    // ENCONTRAR OU CRIAR FATURA
    public Invoice findOrCreateInvoice(CreditCard creditCard, YearMonth yearMonth){
        return invoiceRepository.findByCreditCardAndReferenceMonth(creditCard, yearMonth)
                .orElseGet(() -> createInvoice(yearMonth, creditCard));
    }

    // CRIAR INVOICE
    private Invoice createInvoice(YearMonth yearMonth, CreditCard creditCard){
        LocalDate closingDate = LocalDate.of(
                yearMonth.getYear(),
                yearMonth.getMonth(),
                creditCard.getClosingDayInvoice()
        );

        LocalDate dueDate = closingDate.plusDays(5);

        Invoice invoice = new Invoice(yearMonth, creditCard);

        invoice.setClosingDate(closingDate);
        invoice.setDueDate(dueDate);

        return invoiceRepository.save(invoice);
    }

}
