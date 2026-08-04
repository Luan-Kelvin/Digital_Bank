package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.ENUM.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions", schema = "entitys")
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private BigDecimal transactionValue;

    @Column(nullable = false)
    private LocalDateTime dateAndTime = LocalDateTime.now();

    private String description = "(não informada)";

    @ManyToOne()
    @JoinColumn(name = "id_conta")
    private Account account;

    public Transaction(
           TransactionType transactionType,
           BigDecimal transactionValue,
           String description,
           Account account
    ) {
        this.transactionType = transactionType;
        this.transactionValue = transactionValue;
        setDescription(description);
        this.account = account;
    }

    public void setDescription(String description){

        if (description == null || description.isEmpty()){
            this.description = "(não informada)";
        }else {
            this.description = description;
        }
    }
}
