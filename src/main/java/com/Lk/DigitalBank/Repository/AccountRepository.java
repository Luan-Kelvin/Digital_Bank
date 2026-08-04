package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = """
            SELECT nextval('entitys.account_number_seq')
            """, nativeQuery = true)
    Long nextAccountNumber();
}
