package kz.sec.lms.crm.model;

import ca.utoronto.lms.shared.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
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
}
