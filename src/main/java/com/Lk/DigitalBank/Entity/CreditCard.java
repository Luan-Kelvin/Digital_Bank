package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import org.hibernate.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Table(name = "creditCards", schema = "entitys")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String cardNumber;

    @Column(nullable = false, updatable = false)
    private String CVV;

    @Column(nullable = false)
    private YearMonth expirationDate;

    @Column(nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal usedLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate DueDay;

    @Column(nullable = false, length = 4)
    private String password;

    @OneToOne(mappedBy = "creditCard")
    private Account account;

    public CreditCard(String password) {
        this.password = password;
    }

}
