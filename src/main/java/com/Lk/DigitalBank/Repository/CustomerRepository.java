package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    // BUSCAR CLIENTES ATIVOS
    @Query("""
            SELECT c
            FROM Customer c
            WHERE c.customerStatus = 'ACTIVE'
            """)
    List<Customer> searchActives();

    // BUSCAR CLIENTES INATIVOS
    @Query("""
            SELECT c
            FROM Customer c
            WHERE c.customerStatus = 'INACTIVE'
            """)
    List<Customer> searchInactives();
}
