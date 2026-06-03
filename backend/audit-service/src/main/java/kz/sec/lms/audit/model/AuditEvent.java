package kz.sec.lms.audit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_event", indexes = {
        @Index(name = "idx_occurred", columnList = "occurredAt"),
        @Index(name = "idx_username", columnList = "username, occurredAt"),
        @Index(name = "idx_path", columnList = "path")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false, length = 64)
    private String requestId;

    @Column(length = 255)
    private String username;

    private Long userId;

    @Column(length = 255)
    private String roles;

    @Column(nullable = false, length = 50)
    private String serviceName;

    @Column(nullable = false, length = 10)
    private String httpMethod;

    @Column(nullable = false, length = 500)
    private String path;

    @Column(nullable = false)
    private Integer status;

    @Column(length = 45)
    private String remoteIp;

    @Column(length = 500)
    private String userAgent;

    @Column(name = "is_sensitive", nullable = false)
    private boolean sensitive;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String requestBody;

    @Column(length = 500)
    private String responseSummary;

    private Integer durationMs;
}
