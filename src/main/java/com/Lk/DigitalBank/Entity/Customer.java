package com.Lk.DigitalBank.Entity;

import com.Lk.DigitalBank.Exception.InvalidCPFException;
import com.Lk.DigitalBank.Exception.MinorClientException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers", schema = "entitys")
@Getter
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

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "customer")
    List<Account> accounts = new ArrayList<>();


    public Customer(String name, String cpf, LocalDate dateOfBirth) {
        this.name = name;

        CPFvalidator(cpf);
        this.cpf = cpf;

        verifyAge(dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    private void verifyAge(LocalDate date) {
        if (date == null){
            throw new IllegalArgumentException("Erro! Data inválida");
        }
        Integer age = Period.between(date, LocalDate.now()).getYears();

        if (age < 18) {
            throw new MinorClientException("Erro! É preciso ter 18 anos ou mais para criar conta no banco.");
        }
    }

    private void CPFvalidator(String cpf){
        if (!cpf.matches("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$")){
            throw new InvalidCPFException("ERRO! Formato de CPF inválido");
        }
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        verifyAge(dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public void addAccount(Account account){
        if (account == null){
            throw new IllegalArgumentException("ERRO! Conta inválida.");
        }

        if (!accounts.contains(account)){
            accounts.add(account);
        }

        if (account.getCustomer() != this){
            account.addCustomer(this);
        }
    }

    public void removeAccount(Account account){
        if (accounts.remove(account)){
            account.removeCustomer();
        }
    }
}