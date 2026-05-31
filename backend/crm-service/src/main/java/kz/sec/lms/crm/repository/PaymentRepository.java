package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Payment;
import kz.sec.lms.shared.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends BaseRepository<Payment, Long> {

    List<Payment> findByDeletedFalseOrderByCreatedAtDesc();

    List<Payment> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(String userId);

    List<Payment> findByClientIdAndDeletedFalseOrderByCreatedAtDesc(Long clientId);

    boolean existsByCourseIdAndUserIdAndDeletedFalse(Long courseId, String userId);

    List<Payment> findByStatusAndDeletedFalse(String status);

    List<Payment> findByStatusInAndDeletedFalse(List<String> statuses);

    // Returns [month_key (yyyy-MM), total_revenue] for last 6 months (SUCCESS + COMPLETED)
    @Query(value = "SELECT DATE_FORMAT(created_at, '%Y-%m') AS mk, COALESCE(SUM(amount), 0) FROM payment WHERE deleted = 0 AND status IN ('SUCCESS','COMPLETED') AND created_at >= DATE_SUB(NOW(), INTERVAL 6 MONTH) GROUP BY mk ORDER BY mk ASC", nativeQuery = true)
    List<Object[]> revenueByMonthLast6();
}
