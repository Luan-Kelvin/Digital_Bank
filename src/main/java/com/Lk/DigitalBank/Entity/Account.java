package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Exception.InvalidDepositAmount;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts", schema = "entitys")
@NoArgsConstructor
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private AccountType accountType = AccountType.CURRENT;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customer customer;

    @OneToMany(mappedBy = "account")
    List<Transaction> transactions = new ArrayList<>();

    // ADICIONAR NOVA TRANSAÇÃO FEITA
    public void addTransaction(Transaction transaction){
        if (transaction == null){
            return;
        }

        if (!transactions.contains(transaction)){
            transactions.add(transaction);
        }

        if (transaction.getAccount() != this){
            transaction.setAccount(this);
        }
    }

    // APAGAR TRANSAÇÃO D HISTÓRICO
    public void removeTransaction(Transaction transaction){

        if (transactions.remove(transaction)){
            transaction.setAccount(null);
        }

    }

    // DEPOSITAR DINHEIRO
    public void deposit(BigDecimal value){
        if (value.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidDepositAmount("ERRO! Valor para déposito deve ser maior que 0.");
        }

        balance = balance.add(value);
    }

}
