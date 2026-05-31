package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Client;
import kz.sec.lms.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends BaseRepository<Client, Long> {

    List<Client> findByDeletedFalseOrderByCreatedAtDesc();

    long countByDeletedFalse();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.deleted = false AND c.hasLmsAccount = true")
    long countWithLmsAccount();
}
