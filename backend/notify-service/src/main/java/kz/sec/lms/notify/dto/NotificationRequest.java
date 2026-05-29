package kz.sec.lms.notify.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long userId;
    private String username;
    private String type;
    private String title;
    private String message;
    private String subjectName;
    private String link;
}
