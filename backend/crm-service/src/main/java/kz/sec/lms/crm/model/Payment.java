package kz.sec.lms.crm.model;

import ca.utoronto.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

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

    // last 4 digits of card (null for CASH/TRANSFER)
    @Column(length = 4)
    private String cardLastFour;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private String currency;

    @Column(nullable = false)
    private String status;

    private String userId;

    // CRM-specific fields
    private Long clientId;

    // CARD, CASH, TRANSFER, ONLINE
    private String method;

    private String courseTitle;
}
