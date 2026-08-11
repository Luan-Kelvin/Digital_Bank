package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices", schema = "entitys")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private YearMonth referenceMonth;

    @Column(nullable = false)
    @ToString.Include
    private LocalDate closingDate;

    @Column(nullable = false)
    @ToString.Include
    private LocalDate dueDate;

    @Column(nullable = false)
    @ToString.Include
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private BigDecimal remainingAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus invoiceStatus;

    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "invoice")
    private List<PurchaseInstallment> installments = new ArrayList<>();

    public Invoice(YearMonth referenceMonth, BigDecimal totalAmount, CreditCard creditCard) {
        this.referenceMonth = referenceMonth;
        this.totalAmount = totalAmount;
        this.creditCard = creditCard;
        this.totalAmount = BigDecimal.ZERO;
        this.paidAmount = BigDecimal.ZERO;
        this.remainingAmount = BigDecimal.ZERO;
        this.invoiceStatus = InvoiceStatus.OPEN;

    }

    // ADICIONAR PURCHASE INSTALLMENT
    public void addpurchaseInstallment(PurchaseInstallment installment) {
        if (installment == null) {
            throw new IllegalArgumentException("Parcela inválida.");
        }

        if (!installments.contains(installment)) {
            installments.add(installment);
        }

        installment.associateInvoice(this);

        totalAmount = totalAmount.add(installment.getAmount());

        remainingAmount = remainingAmount.add(installment.getAmount());
    }

    public void makePayment(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de pagamento inválido.");
        }

        if (amount.compareTo(remainingAmount) > 0) {
            throw new IllegalArgumentException(
                    "Pagamento maior que o valor restante da fatura."
            );
        }

        paidAmount = paidAmount.add(amount);
        remainingAmount = remainingAmount.subtract(amount);

        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            invoiceStatus = InvoiceStatus.PAID;
            paymentDate = LocalDateTime.now();
        }

    }
}
