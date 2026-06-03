package kz.sec.lms.audit.controller;

import kz.sec.lms.audit.dto.AuditEventDTO;
import kz.sec.lms.audit.service.AuditEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditEventService service;

    @Value("${audit.internal-token:dev-internal-token}")
    private String internalToken;

    public AuditController(AuditEventService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<Void> create(
            @RequestHeader("X-Internal-Token") String token,
            @RequestBody AuditEventDTO event) {
        if (!internalToken.equals(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        service.save(event);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/events")
    public ResponseEntity<Page<AuditEventDTO>> search(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String path,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Boolean sensitive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(service.search(from, to, username, method, path, status, sensitive, page, size));
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<AuditEventDTO> findOne(@PathVariable Long id) {
        AuditEventDTO event = service.findById(id);
        return event == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(event);
    }
}
