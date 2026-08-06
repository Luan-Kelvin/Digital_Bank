package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.Exception.InvalidCreditCradPINException;
import jakarta.persistence.*;

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
    private LocalDate expirationDate;

    @Column(nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal usedLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate dueDay;

    @Column(nullable = false, length = 4)
    private String password;

    @OneToOne(mappedBy = "creditCard")
    private Account account;

    public CreditCard(String password) {
        verifyPassword(password);
        this.password = password;
    }

    // VERIFICAR SENHA
    private  void verifyPassword(String password){
        if (password.length() != 4){
            throw new InvalidCreditCradPINException("ERRO! Senha deve conter exatamente 4 digitos");
        }

        if (!password.matches("^[0-9]{4}$")){
            throw new InvalidCreditCradPINException("ERRO! senha deve conter apenas números.");
        }

        if (password.equalsIgnoreCase("1234")){
            throw new InvalidCreditCradPINException("ERRO! Senha fraca, não pode ser "+password);
        }
    }
}
