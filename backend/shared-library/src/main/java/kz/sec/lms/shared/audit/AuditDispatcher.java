package kz.sec.lms.shared.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Async dispatcher for audit events. Kept separate from AuditFilter because
 * @Async forces proxying, which breaks OncePerRequestFilter (whose final
 * doFilter/init methods can't be overridden by CGLib).
 */
@Slf4j
@Component
public class AuditDispatcher {

    @Autowired(required = false)
    private AuditClient auditClient;

    @Value("${audit.internal-token:}")
    private String internalToken;

    @Value("${audit.fallback-file:/var/log/sec/audit-fallback.log}")
    private String fallbackFile;

    @Async("auditExecutor")
    public void dispatch(AuditEventDTO event) {
        if (auditClient == null) {
            writeFallback(event);
            return;
        }
        try {
            auditClient.postEvent(internalToken, event);
        } catch (Exception e) {
            log.debug("audit-service unreachable, writing fallback: {}", e.getMessage());
            writeFallback(event);
        }
    }

    private void writeFallback(AuditEventDTO event) {
        try {
            Path file = Path.of(fallbackFile);
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            try (BufferedWriter w = Files.newBufferedWriter(file,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                w.write(event.toString());
                w.newLine();
            }
        } catch (Exception fbErr) {
            log.error("Audit fallback write failed: {}", fbErr.getMessage());
        }
    }
}
