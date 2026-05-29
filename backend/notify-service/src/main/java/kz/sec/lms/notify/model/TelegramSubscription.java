package kz.sec.lms.notify.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelegramSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "telegram_username")
    private String telegramUsername;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "notify_grades")
    private boolean notifyGrades = true;

    @Column(name = "notify_exams")
    private boolean notifyExams = true;

    @Column(name = "notify_materials")
    private boolean notifyMaterials = true;

    @Column(name = "notify_announcements")
    private boolean notifyAnnouncements = true;
}
