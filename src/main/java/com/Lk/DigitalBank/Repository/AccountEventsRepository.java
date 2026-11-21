package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.AccountEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountEventsRepository extends JpaRepository<AccountEvents, Long> {

    @Query("""
            SELECT COALESCE(MAX(e.sequenceNumber), -1)
            FROM AccountEvents e
            WHERE e.account.id = :accountId
            """)
    Long findLastSequenceNumber(@Param("accountId") Long accountId);

}
