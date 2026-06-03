package kz.sec.lms.audit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class RetentionCleanupJob {

    private final AuditEventService service;

    @Value("${audit.retention-days:90}")
    private int retentionDays;

    public RetentionCleanupJob(AuditEventService service) {
        this.service = service;
    }

    /** Runs daily at 03:00 server-local. */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanup() {
        Instant cutoff = Instant.now().minus(Duration.ofDays(retentionDays));
        int deleted = service.deleteOlderThan(cutoff);
        log.info("Audit retention cleanup deleted {} events older than {} days", deleted, retentionDays);
    }
}
