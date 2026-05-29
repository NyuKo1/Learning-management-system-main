package kz.sec.lms.notify.controller;

import kz.sec.lms.notify.dto.NotificationRequest;
import kz.sec.lms.notify.model.TelegramSubscription;
import kz.sec.lms.notify.repository.TelegramSubscriptionRepository;
import kz.sec.lms.notify.service.TelegramNotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/link")
    public ResponseEntity<Map<String, Object>> linkUser(@RequestBody Map<String, Object> body) {
        Long userId = Long.parseLong(body.get("userId").toString());
        String username = body.get("username").toString();

        Optional<TelegramSubscription> sub = subscriptionRepo.findByUsername(username);
        if (sub.isPresent()) {
            TelegramSubscription s = sub.get();
            s.setUserId(userId);
            subscriptionRepo.save(s);
            return ResponseEntity.ok(Map.of("linked", true));
        }
        return ResponseEntity.ok(Map.of("linked", false, "reason", "No Telegram subscription for username"));
    }
}
