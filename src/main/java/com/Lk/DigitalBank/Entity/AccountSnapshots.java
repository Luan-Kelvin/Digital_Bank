package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Join;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_snapshots", schema = "entitys")
@NoArgsConstructor
@Getter
public class AccountSnapshots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private Long lastSequenceNumber;


    private LocalDateTime createdAt;

    public AccountSnapshots(Account account, BigDecimal balance, Long lastSequenceNumber) {
        this.account = account;
        this.balance = balance;
        this.lastSequenceNumber = lastSequenceNumber;
        this.createdAt = LocalDateTime.now();
    }

    public void updateSnapshot(BigDecimal balance, Long lastSequenceNumber) {
        this.balance = balance;
        this.lastSequenceNumber = lastSequenceNumber;
        this.createdAt = LocalDateTime.now();
    }
}
