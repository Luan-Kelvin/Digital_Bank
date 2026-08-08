package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "credit_card_purchases", schema = "entitys")
@Getter
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class CreditCardPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @ToString.Include
    private BigDecimal ammountPurchase;

    @Column(nullable = false)
    @ToString.Include
    private LocalDate purchaseDate = LocalDate.now();

    @Column(nullable = false)
    @ToString.Include
    private Integer installments = 0;

    @Column(nullable = false)
    @ToString.Include
    private BigDecimal installmentAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    @ToString.Include
    private String merchant;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    public CreditCardPurchase(String description, BigDecimal ammount, Integer installments, String merchant, CreditCard creditCard) {
        this.description = description;
        this.ammountPurchase = ammount;
        this.installments = installments;
        this.merchant = merchant;
        this.creditCard = creditCard;
        calculateIstallment(ammountPurchase, installments);
    }

    //CALCULAR VALOR DAS PARCELAS
    private void calculateIstallment(BigDecimal ammountPurchase, Integer installments){
        if (installments <= 1){
            this.installmentAmount = ammountPurchase;
            return;
        }

        this.installmentAmount = ammountPurchase.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);
    }

}
