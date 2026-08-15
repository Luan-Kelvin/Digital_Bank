package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "credit_card_purchases", schema = "entitys")
@Getter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class CreditCardPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(nullable = false)
    @ToString.Include
    private BigDecimal amountPurchase;

    @Column(nullable = false)
    @ToString.Include
    private LocalDateTime purchaseDate = LocalDateTime.now();

    @Column(nullable = false)
    @ToString.Include
    private Integer installments = 1;

    @Column(nullable = false)
    @ToString.Include
    private BigDecimal installmentAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @ToString.Include
    private String merchant;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    @OneToMany(mappedBy = "creditCardPurchase", cascade = CascadeType.ALL)
    private List<PurchaseInstallment> purchaseInstallments = new ArrayList<>();

    public CreditCardPurchase(String description, BigDecimal amount, Integer installments, String merchant, CreditCard creditCard) {
        validatePurchase(amount, installments);
        this.description = description;
        this.amountPurchase = amount;
        this.installments = installments;
        this.merchant = merchant;
        this.creditCard = creditCard;
        calculateInstallment(amountPurchase, installments);
    }

    //CALCULAR VALOR DAS PARCELAS
    private void calculateInstallment(BigDecimal amountPurchase, Integer installments){
        if (installments <= 1){
            this.installmentAmount = amountPurchase;
            return;
        }

        this.installmentAmount = amountPurchase.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
    }

    //VALIDAR VALOR E NUMERO D PARCELAS DA COMPRA
    private void validatePurchase(BigDecimal amount, Integer Installments){
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Compra deve ser maior que 0.");
        }

        if (installments == null || installments <= 0){
            throw new IllegalArgumentException("Número de parcelas deve ser maior que 0.");
        }
    }

    // ADICIONANDO CARTÃO DE CRÉDITO
    public void addCrditCard(CreditCard creditCard){
        this.creditCard = creditCard;
    }

    // ADICIONANDO PARCELAS
    public void addInstallments(PurchaseInstallment installment){
        if (installment == null){
                throw new IllegalArgumentException("ERRO! installment não pode ser nulo.");
        }

        purchaseInstallments.add(installment);
        installment.setCreditCardPurchase(this);
    }

}
