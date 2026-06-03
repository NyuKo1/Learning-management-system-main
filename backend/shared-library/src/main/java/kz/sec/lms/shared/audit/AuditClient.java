package kz.sec.lms.shared.audit;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "audit-service", contextId = "auditClient")
public interface AuditClient {

    @PostMapping("/audit/events")
    ResponseEntity<Void> postEvent(
            @RequestHeader("X-Internal-Token") String internalToken,
            @RequestBody AuditEventDTO event
    );
}
