package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
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
    private BigDecimal ammount;

    @Column(nullable = false)
    private LocalDate purchaseDate = LocalDate.now();

    @Column(nullable = false)
    private Integer installments = 0;

    @Column(nullable = false)
    private BigDecimal installmentAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private String merchant;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    public CreditCardPurchase(String description, BigDecimal ammount, Integer installments, String merchant, CreditCard creditCard) {
        this.description = description;
        this.ammount = ammount;
        this.installments = installments;
        this.merchant = merchant;
        this.creditCard = creditCard;
    }

}
