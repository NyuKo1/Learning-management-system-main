package ca.utoronto.lms.crm.repository;

import ca.utoronto.lms.crm.model.Payment;
import ca.utoronto.lms.shared.repository.BaseRepository;

import java.util.List;

public interface PaymentRepository extends BaseRepository<Payment, Long> {

    List<Payment> findByUserIdAndDeletedFalseOrderByCreatedAtDesc(String userId);

    boolean existsByCourseIdAndUserIdAndDeletedFalse(Long courseId, String userId);
}
