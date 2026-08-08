package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.CardStatus;
import com.Lk.DigitalBank.Exception.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "creditCards", schema = "entitys")
@Getter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @ToString.Include
    private String cardNumber;

    @Column(nullable = false, updatable = false)
    @ToString.Include
    private String cvv;

    @Column(nullable = false)
    @ToString.Include
    private LocalDate expirationDate = LocalDate.now().plusYears(5);

    @Column(nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal avalialableLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal usedLimit = BigDecimal.ZERO;

    @Column(nullable = false)
    private CardStatus cardStatus = CardStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate dueDay = LocalDate.now().plusDays(20);;

    @Column(nullable = false, length = 4)
    private String password;

    @OneToOne(mappedBy = "creditCard")
    private Account account;

    public CreditCard(String password, String cardNumber, Account account) {
        verifyPassword(password);
        this.password = password;
        this.cardNumber = cardNumber;
        this.cvv = cardNumber.split(" ")[3];
        this.account = account;
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
