package kz.sec.lms.notify.repository;

import kz.sec.lms.notify.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByUserIdOrderBySentAtDesc(Long userId);
}
