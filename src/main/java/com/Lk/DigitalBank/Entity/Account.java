package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts", schema = "entitys")
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    private Customer customer;

    @OneToMany(mappedBy = "account")
    List<Transaction> transactions = new ArrayList<>();

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

    public void removeTransaction(Transaction transaction){

        if (transactions.remove(transaction)){
            transaction.setAccount(null);
        }

    }

}
