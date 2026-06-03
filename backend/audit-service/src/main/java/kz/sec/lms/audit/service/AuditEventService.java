package kz.sec.lms.audit.service;

import kz.sec.lms.audit.dto.AuditEventDTO;
import kz.sec.lms.audit.model.AuditEvent;
import kz.sec.lms.audit.repository.AuditEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditEventService {

    private final AuditEventRepository repository;

    public AuditEventService(AuditEventRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AuditEventDTO save(AuditEventDTO dto) {
        AuditEvent entity = AuditEvent.builder()
                .occurredAt(dto.getOccurredAt() == null ? Instant.now() : dto.getOccurredAt())
                .requestId(dto.getRequestId())
                .username(dto.getUsername())
                .userId(dto.getUserId())
                .roles(dto.getRoles() == null ? "" : String.join(",", dto.getRoles()))
                .serviceName(dto.getServiceName())
                .httpMethod(dto.getHttpMethod())
                .path(dto.getPath())
                .status(dto.getStatus())
                .remoteIp(dto.getRemoteIp())
                .userAgent(dto.getUserAgent())
                .sensitive(dto.isSensitive())
                .requestBody(dto.getRequestBody())
                .responseSummary(dto.getResponseSummary())
                .durationMs(dto.getDurationMs())
                .build();
        return toDto(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public Page<AuditEventDTO> search(Instant from, Instant to, String username,
                                       String method, String path, Integer status,
                                       Boolean sensitive, int page, int size) {
        return repository.search(from, to, username, method, path, status, sensitive,
                        PageRequest.of(page, size))
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public AuditEventDTO findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    public int deleteOlderThan(Instant cutoff) {
        return repository.deleteOlderThan(cutoff);
    }

    private AuditEventDTO toDto(AuditEvent e) {
        List<String> roles = (e.getRoles() == null || e.getRoles().isBlank())
                ? List.of()
                : Arrays.stream(e.getRoles().split(",")).collect(Collectors.toList());
        return AuditEventDTO.builder()
                .id(e.getId())
                .occurredAt(e.getOccurredAt())
                .requestId(e.getRequestId())
                .username(e.getUsername())
                .userId(e.getUserId())
                .roles(roles)
                .serviceName(e.getServiceName())
                .httpMethod(e.getHttpMethod())
                .path(e.getPath())
                .status(e.getStatus())
                .remoteIp(e.getRemoteIp())
                .userAgent(e.getUserAgent())
                .sensitive(e.isSensitive())
                .requestBody(e.getRequestBody())
                .responseSummary(e.getResponseSummary())
                .durationMs(e.getDurationMs())
                .build();
    }
}
