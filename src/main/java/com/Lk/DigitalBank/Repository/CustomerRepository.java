package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
