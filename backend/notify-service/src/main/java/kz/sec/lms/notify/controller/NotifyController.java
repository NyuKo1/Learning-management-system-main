package kz.sec.lms.notify.controller;

import kz.sec.lms.notify.dto.NotificationRequest;
import kz.sec.lms.notify.model.TelegramSubscription;
import kz.sec.lms.notify.repository.TelegramSubscriptionRepository;
import kz.sec.lms.notify.service.TelegramNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class NotifyController {

    private final TelegramNotifyService telegramService;
    private final TelegramSubscriptionRepository subscriptionRepo;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(@RequestBody NotificationRequest request) {
        boolean sent = telegramService.sendNotification(request);
        return ResponseEntity.ok(Map.of("sent", sent));
    }

    @GetMapping("/subscription/{userId}")
    public ResponseEntity<Map<String, Object>> getSubscription(@PathVariable Long userId) {
        Optional<TelegramSubscription> sub = subscriptionRepo.findByUserIdAndActiveTrue(userId);
        Map<String, Object> result = new HashMap<>();
        if (sub.isPresent()) {
            result.put("active", true);
            result.put("telegramUsername", sub.get().getTelegramUsername() == null ? "" : sub.get().getTelegramUsername());
            result.put("createdAt", sub.get().getCreatedAt().toString());
        } else {
            result.put("active", false);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Links the authenticated user's LMS account to their Telegram subscription.
     * The username is taken from the JWT principal (body.username is ignored to
     * prevent users from linking someone else's Telegram).
     */
    @PostMapping("/link")
    public ResponseEntity<Map<String, Object>> linkUser(
            Authentication authentication,
            @RequestBody Map<String, Object> body) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("linked", false, "reason", "unauthenticated"));
        }
        String username = authentication.getName();
        Long userId = body.get("userId") != null
                ? Long.parseLong(body.get("userId").toString())
                : null;

        Optional<TelegramSubscription> sub = subscriptionRepo.findByUsername(username);
        if (sub.isPresent()) {
            TelegramSubscription s = sub.get();
            if (userId != null) {
                s.setUserId(userId);
            }
            subscriptionRepo.save(s);
            return ResponseEntity.ok(Map.of("linked", true));
        }
        return ResponseEntity.ok(Map.of("linked", false, "reason", "No Telegram subscription for username"));
    }

    @GetMapping("/bot-info")
    public ResponseEntity<Map<String, String>> getBotInfo() {
        Map<String, String> info = new HashMap<>();
        if (telegramService.isBotConfigured()) {
            info.put("username", telegramService.getBotUsername());
            info.put("configured", "true");
        } else {
            info.put("username", "");
            info.put("configured", "false");
        }
        return ResponseEntity.ok(info);
    }
}
