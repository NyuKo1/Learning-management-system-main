package kz.sec.lms.notify.repository;

import kz.sec.lms.notify.model.TelegramSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelegramSubscriptionRepository extends JpaRepository<TelegramSubscription, Long> {
    Optional<TelegramSubscription> findByChatId(Long chatId);
    Optional<TelegramSubscription> findByUsername(String username);
    List<TelegramSubscription> findByActiveTrue();
    Optional<TelegramSubscription> findByUserIdAndActiveTrue(Long userId);
    boolean existsByChatId(Long chatId);
}
