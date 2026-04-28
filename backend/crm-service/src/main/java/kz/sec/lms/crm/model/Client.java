package kz.sec.lms.crm.model;

import ca.utoronto.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client extends BaseEntity<Long> {

    @Column(nullable = false)
    private String fullName;

    private String phone;
    private String email;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer totalPurchases = 0;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalSpent;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean hasLmsAccount = false;

    private Long lmsUserId;

    @Lob
    private String notes;
}
