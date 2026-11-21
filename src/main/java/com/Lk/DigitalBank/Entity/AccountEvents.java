package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.EventType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "account_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"account_id", "sequence_number"}
                )
        }
)
@Getter
@NoArgsConstructor
public class AccountEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @Column(name = "sequence_number", nullable = false)
    private Long sequenceNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private EventType eventType;

    @NotNull
    @Positive
    @Column(nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime occurredAt;

    public AccountEvents(Account account, EventType eventType, BigDecimal amount, Long sequenceNumber) {
        this.account = account;
        this.sequenceNumber = sequenceNumber;
        this.eventType = eventType;
        this.amount = amount;
        this.occurredAt = LocalDateTime.now();
    }
}
