package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // BUSCAR POR NOME
    List<Customer> findByNameContainingIgnoreCase(String name);

    // BUSCAR POR CPF
    Optional<Customer> findByCpf(String cpf);

    // BUSCAR POR DATA DE NASCIMENTO
    List<Customer> findByDateOfBirth(LocalDate date);

    // VER SE JA EXISTE CLIENTE COM CPF
    Boolean existsByCpf(String cpf);
}
