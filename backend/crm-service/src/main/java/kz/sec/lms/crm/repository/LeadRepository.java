package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Lead;
import ca.utoronto.lms.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeadRepository extends BaseRepository<Lead, Long> {

    List<Lead> findByDeletedFalseOrderByCreatedAtDesc();

    List<Lead> findByStatusAndDeletedFalse(String status);

    long countByStatusAndDeletedFalse(String status);

    long countByDeletedFalse();

    // Returns [month_key (yyyy-MM), count] for last 6 months
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS mk, COUNT(*) FROM lead WHERE deleted = 0 AND created_at >= DATE_SUB(NOW(), INTERVAL 6 MONTH) GROUP BY mk ORDER BY mk ASC", nativeQuery = true)
    List<Object[]> countByMonthLast6();
}
