package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
