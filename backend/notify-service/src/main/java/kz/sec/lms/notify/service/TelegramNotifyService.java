package kz.sec.lms.notify.service;

import kz.sec.lms.notify.bot.SecNotifyBot;
import kz.sec.lms.notify.dto.NotificationRequest;
import kz.sec.lms.notify.model.NotificationLog;
import kz.sec.lms.notify.model.TelegramSubscription;
import kz.sec.lms.notify.repository.NotificationLogRepository;
import kz.sec.lms.notify.repository.TelegramSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class TelegramNotifyService {

    private final SecNotifyBot bot;
    private final TelegramSubscriptionRepository subscriptionRepo;
    private final NotificationLogRepository logRepo;

    public TelegramNotifyService(
            @Autowired(required = false) SecNotifyBot bot,
            TelegramSubscriptionRepository subscriptionRepo,
            NotificationLogRepository logRepo) {
        this.bot = bot;
        this.subscriptionRepo = subscriptionRepo;
        this.logRepo = logRepo;
    }

    public boolean isBotConfigured() {
        return bot != null;
    }

    public String getBotUsername() {
        return bot != null ? bot.getBotUsername() : "";
    }

    public boolean sendNotification(NotificationRequest request) {
        if (bot == null) {
            log.warn("Telegram bot not configured — skipping notification for user {}", request.getUsername());
            return false;
        }

        Optional<TelegramSubscription> subOpt = findSubscription(request);

        if (subOpt.isEmpty()) {
            log.debug("No active Telegram subscription for user {} / {}",
                    request.getUserId(), request.getUsername());
            return false;
        }

        TelegramSubscription sub = subOpt.get();

        if (!isTypeEnabled(sub, request.getType())) {
            log.debug("Notification type {} disabled for user {}", request.getType(), request.getUsername());
            return false;
        }

        String text = formatMessage(request);
        boolean success = false;
        String errorMsg = null;

        try {
            bot.sendText(sub.getChatId(), text);
            success = true;
        } catch (Exception e) {
            log.error("Failed to send notification to {}: {}", sub.getChatId(), e.getMessage());
            errorMsg = e.getMessage();
        }

        logRepo.save(NotificationLog.builder()
                .userId(request.getUserId())
                .chatId(sub.getChatId())
                .messageType(request.getType())
                .messageText(text)
                .sentAt(LocalDateTime.now())
                .success(success)
                .errorMessage(errorMsg)
                .build());

        return success;
    }

    private Optional<TelegramSubscription> findSubscription(NotificationRequest request) {
        if (request.getUserId() != null) {
            Optional<TelegramSubscription> byUserId = subscriptionRepo.findByUserIdAndActiveTrue(request.getUserId());
            if (byUserId.isPresent()) return byUserId;
        }
        if (request.getUsername() != null) {
            return subscriptionRepo.findByUsername(request.getUsername())
                    .filter(TelegramSubscription::isActive);
        }
        return Optional.empty();
    }

    private boolean isTypeEnabled(TelegramSubscription sub, String type) {
        if (type == null) return true;
        return switch (type) {
            case "GRADE" -> sub.isNotifyGrades();
            case "EXAM" -> sub.isNotifyExams();
            case "MATERIAL" -> sub.isNotifyMaterials();
            case "ANNOUNCEMENT" -> sub.isNotifyAnnouncements();
            default -> true;
        };
    }

    private String formatMessage(NotificationRequest req) {
        String type = req.getType() == null ? "" : req.getType();
        String emoji = switch (type) {
            case "GRADE" -> "📝";
            case "EXAM" -> "📅";
            case "MATERIAL" -> "📚";
            case "ANNOUNCEMENT" -> "📢";
            default -> "🔔";
        };

        StringBuilder sb = new StringBuilder();
        sb.append(emoji).append(" *").append(req.getTitle() == null ? "Уведомление" : req.getTitle()).append("*\n\n");
        if (req.getMessage() != null) {
            sb.append(req.getMessage()).append("\n");
        }
        if (req.getSubjectName() != null && !req.getSubjectName().isBlank()) {
            sb.append("\n*Предмет:* ").append(req.getSubjectName());
        }
        if (req.getLink() != null && !req.getLink().isBlank()) {
            sb.append("\n[Открыть в LMS](").append(req.getLink()).append(")");
        }
        return sb.toString();
    }
}
