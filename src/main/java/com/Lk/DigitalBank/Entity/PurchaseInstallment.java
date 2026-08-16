package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "purchaseInstallment", schema = "entitys")
@Getter
@Setter
@NoArgsConstructor
public class PurchaseInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer installmentNumber;

    @Column(nullable = false)
    private Integer totalInstallments;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus installmentStatus;

    @ManyToOne
    @JoinColumn(name = "credit_card_urchase_id")
    private CreditCardPurchase creditCardPurchase;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public PurchaseInstallment(Integer installmentNumber,
                               Integer totalInstallments,
                               BigDecimal amount,
                               InvoiceStatus installmentStatus,
                               CreditCardPurchase creditCardPurchase,
                               Invoice invoice) {
        this.installmentNumber = installmentNumber;
        this.totalInstallments = totalInstallments;
        this.amount = amount;
        this.installmentStatus = installmentStatus;
        this.creditCardPurchase = creditCardPurchase;
        this.invoice = invoice;
    }

    // ASSOCIAR INSTALLMENT COM CREDITCARDPRUCHASE
    public void associatePurchase(CreditCardPurchase purchase){
        if (purchase == null) {
            throw new IllegalArgumentException("ERRO! Compra no cartão de credito inválida.");
        }

         purchase.addInstallments(this);
    }

    // ASSOCIAR INSTALLMENT COM INVOICE
    public void associateInvoice(Invoice invoice){
        if (invoice == null){
            throw new IllegalArgumentException("ERRO! Fatura inválida.");
        }

        this.invoice = invoice;
    }
}
