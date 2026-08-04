package com.Lk.DigitalBank.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "clients", schema = "entitys")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dateOfBirth;


    public Customer(String name, String cpf, LocalDate dateOfBirth) {
        this.name = name;
        this.cpf = cpf;
        this.dateOfBirth = dateOfBirth;
    }


}
