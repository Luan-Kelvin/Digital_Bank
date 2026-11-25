package com.Lk.DigitalBank.Repository;

import com.Lk.DigitalBank.Entity.AccountSnapshots;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountSnapshotsRepository extends JpaRepository<AccountSnapshots, Long> {

    Optional<AccountSnapshots> findByAccount_Id(Long account_id);

    Boolean existsByAccount_id(Long idAccount);
}
