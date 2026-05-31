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
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseLesson extends BaseEntity<Long> {

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    private String videoUrl;

    @Lob
    private String content;

    @Column(nullable = false)
    private Integer orderIndex;

    private String duration;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}