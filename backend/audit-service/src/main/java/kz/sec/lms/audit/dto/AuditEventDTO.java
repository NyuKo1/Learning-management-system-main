package kz.sec.lms.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventDTO {
    private Long id;
    private Instant occurredAt;
    private String requestId;
    private String username;
    private Long userId;
    private List<String> roles;
    private String serviceName;
    private String httpMethod;
    private String path;
    private Integer status;
    private String remoteIp;
    private String userAgent;
    private boolean sensitive;
    private String requestBody;
    private String responseSummary;
    private Integer durationMs;
}
