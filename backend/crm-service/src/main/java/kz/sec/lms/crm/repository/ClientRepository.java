package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Client;
import ca.utoronto.lms.shared.repository.BaseRepository;

import java.util.List;

public interface ClientRepository extends BaseRepository<Client, Long> {

    List<Client> findByDeletedFalseOrderByCreatedAtDesc();

    long countByDeletedFalse();
}
