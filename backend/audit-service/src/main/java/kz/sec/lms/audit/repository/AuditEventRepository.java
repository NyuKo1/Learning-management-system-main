package kz.sec.lms.audit.repository;

import kz.sec.lms.audit.model.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface AuditEventRepository extends JpaRepository<AuditEvent, Long> {

    @Query("SELECT e FROM AuditEvent e " +
            "WHERE (:from IS NULL OR e.occurredAt >= :from) " +
            "  AND (:to   IS NULL OR e.occurredAt <= :to) " +
            "  AND (:username IS NULL OR e.username = :username) " +
            "  AND (:method   IS NULL OR e.httpMethod = :method) " +
            "  AND (:path     IS NULL OR e.path LIKE CONCAT('%', :path, '%')) " +
            "  AND (:status   IS NULL OR e.status = :status) " +
            "  AND (:sensitive IS NULL OR e.sensitive = :sensitive) " +
            "ORDER BY e.occurredAt DESC")
    Page<AuditEvent> search(
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("username") String username,
            @Param("method") String method,
            @Param("path") String path,
            @Param("status") Integer status,
            @Param("sensitive") Boolean sensitive,
            Pageable pageable);

    @Modifying
    @Query("DELETE FROM AuditEvent e WHERE e.occurredAt < :cutoff")
    int deleteOlderThan(@Param("cutoff") Instant cutoff);
}
