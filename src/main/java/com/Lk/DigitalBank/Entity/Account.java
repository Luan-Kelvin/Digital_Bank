package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.Exception.InsufficientBalanceWithdraw;
import com.Lk.DigitalBank.Exception.InvalidDepositAmountException;
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

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCard creditCard;

    public Account(Customer customer, AccountType accountType) {
        this.customer = customer;
        this.accountType = accountType;
    }

    // VERIFICAR STATUS DE CONTA
    public boolean isActive(){
        return this.accountStatus == AccountStatus.ACTIVE;
    }

    //VERIFICAR SE JA POSSUI CARTÃO DE CRÉDITO
    public boolean isCreditCard(){
        return this.creditCard != null;
    }

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

    // ADICIONAR NÚMERO DE CONTA
    public void addNumberAccount(String number){
        this.accountNumber = number;
    }

    // APAGAR TRANSAÇÃO DO HISTÓRICO
    public void removeTransaction(Transaction transaction){

        if (transactions.remove(transaction)){
            transaction.setAccount(null);
        }

    }

    // DEPOSITAR DINHEIRO
    public void deposit(BigDecimal valueDeposit){
        if (valueDeposit.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidDepositAmountException("ERRO! Valor para déposito deve ser maior que 0.");
        }

        balance = balance.add(valueDeposit);
    }

    // SACAR DINHEIRO
    public void withdraw(BigDecimal value){
        if (value.compareTo(balance) > 0){
            throw new InsufficientBalanceWithdraw("ERRO! saldo insuficiente para saque.");
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidDepositAmountException("ERRO! Valor para déposito deve ser maior que 0.");
        }

        balance = balance.subtract(value);
    }

    //ADICIONAR CLIENTE
    public void addCustomer(Customer customer){
        this.customer = customer;
    }


    //REMOVER CLIENTE
    public void removeCustomer(){
        this.customer = null;
    }

    //ADICIONAR CARTÂO DE CRÉDITO
    public void addCreditCard(CreditCard creditCard){
        this.creditCard = creditCard;
    }

    // DESATIVAR CONTA
    public void blockedAccount(){
        this.accountStatus = AccountStatus.BLOCKED;
    }
}
