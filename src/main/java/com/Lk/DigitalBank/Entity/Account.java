package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.*;

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

}
