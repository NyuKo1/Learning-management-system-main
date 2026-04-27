package kz.sec.lms.crm.repository;

import kz.sec.lms.crm.model.Payment;
import ca.utoronto.lms.shared.repository.BaseRepository;

import java.util.List;

public interface PaymentRepository extends BaseRepository<Payment, Long> {

    List<Payment> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(String userId);

    boolean existsByCourseIdAndUserIdAndDeletedFalse(Long courseId, String userId);
}
