package kz.sec.lms.crm.model;

import kz.sec.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment extends BaseEntity<Long> {

    private Long courseId;

    @Column(nullable = false)
    private String customerName;

    private String email;

    @Column(length = 4)
    private String cardLastFour;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private String currency;

    @Column(nullable = false)
    private String status;

    private String userId;

    private Long clientId;

    private String method;

    private String courseTitle;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}