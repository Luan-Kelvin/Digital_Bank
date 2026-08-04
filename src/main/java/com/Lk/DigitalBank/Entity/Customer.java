package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.Exception.MinorClientException;
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

        verifyAge(dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    private void verifyAge(LocalDate date) {
        Integer age = Period.between(date, LocalDate.now()).getYears();

        if (age < 18) {
            throw new MinorClientException("Erro! É preciso ter 18 anos ou mais para criar conta no banco.");
        }
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        verifyAge(dateOfBirth);
    }
}