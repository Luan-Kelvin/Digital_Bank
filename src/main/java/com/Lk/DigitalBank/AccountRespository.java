package com.Lk.DigitalBank;

import com.Lk.DigitalBank.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRespository extends JpaRepository<Account, Long> {
    
}
