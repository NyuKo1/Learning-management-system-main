package kz.sec.lms.crm.model;

import kz.sec.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "crm_lead")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Lead extends BaseEntity<Long> {

    @Column(nullable = false)
    private String fullName;

    private String phone;
    private String email;

    // WEBSITE, REFERRAL, SOCIAL, COLD_CALL, AD, OTHER
    private String source;

    // NEW, CONTACTED, QUALIFIED, PROPOSAL, WON, LOST
    @Column(nullable = false)
    private String status;

    @Lob
    private String notes;

    private Long interestedCourseId;
    private String interestedCourseTitle;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}