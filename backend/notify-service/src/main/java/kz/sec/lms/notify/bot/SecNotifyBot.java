package kz.sec.lms.notify.bot;

import kz.sec.lms.notify.model.TelegramSubscription;
import kz.sec.lms.notify.repository.TelegramSubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "telegram.bot.token", matchIfMissing = false)
public class SecNotifyBot extends TelegramLongPollingBot {

    private final TelegramSubscriptionRepository subscriptionRepo;

    @Value("${telegram.bot.token:}")
    private String botToken;

    @Value("${telegram.bot.username:}")
    private String botUsername;

    public SecNotifyBot(TelegramSubscriptionRepository subscriptionRepo) {
        this.subscriptionRepo = subscriptionRepo;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();
        String telegramUser = update.getMessage().getFrom().getUserName();

        try {
            if (text.startsWith("/start")) {
                handleStart(chatId, text, telegramUser);
            } else if (text.equals("/stop")) {
                handleStop(chatId);
            } else if (text.equals("/status")) {
                handleStatus(chatId);
            } else if (text.equals("/help")) {
                handleHelp(chatId);
            } else {
                sendText(chatId, "Неизвестная команда. Введите /help для помощи.");
            }
        } catch (Exception e) {
            log.error("Telegram update handling failed: {}", e.getMessage(), e);
        }
    }

    private void handleStart(Long chatId, String text, String telegramUser) {
        String[] parts = text.split("\\s+", 2);

        if (parts.length < 2) {
            sendText(chatId,
                    "👋 *Добро пожаловать в SEC Notify!*\n\n" +
                            "Чтобы подключить уведомления:\n" +
                            "1. Войдите в LMS\n" +
                            "2. Откройте *Мой профиль* → *Telegram*\n" +
                            "3. Скопируйте команду подключения\n\n" +
                            "Или введите: `/start ваш_email@example.com`");
            return;
        }

        String lmsUsername = parts[1].trim();

        Optional<TelegramSubscription> existing = subscriptionRepo.findByChatId(chatId);
        if (existing.isPresent()) {
            TelegramSubscription sub = existing.get();
            sub.setUsername(lmsUsername);
            sub.setActive(true);
            sub.setTelegramUsername(telegramUser);
            subscriptionRepo.save(sub);
            sendText(chatId, "✅ Подписка обновлена для `" + lmsUsername + "`");
            return;
        }

        TelegramSubscription sub = TelegramSubscription.builder()
                .chatId(chatId)
                .username(lmsUsername)
                .telegramUsername(telegramUser)
                .active(true)
                .createdAt(LocalDateTime.now())
                .notifyGrades(true)
                .notifyExams(true)
                .notifyMaterials(true)
                .notifyAnnouncements(true)
                .build();

        subscriptionRepo.save(sub);
        sendText(chatId,
                "✅ *Подписка активирована!*\n\n" +
                        "Email: `" + lmsUsername + "`\n\n" +
                        "Вы будете получать уведомления о:\n" +
                        "• 📝 Оценках\n• 📅 Экзаменах\n• 📚 Материалах\n• 📢 Объявлениях\n\n" +
                        "Команды:\n/stop — отписаться\n/status — статус подписки\n/help — помощь");
    }

    private void handleStop(Long chatId) {
        Optional<TelegramSubscription> sub = subscriptionRepo.findByChatId(chatId);
        if (sub.isPresent()) {
            TelegramSubscription s = sub.get();
            s.setActive(false);
            subscriptionRepo.save(s);
            sendText(chatId, "❌ Вы отписались от уведомлений.\nДля повторной подписки введите /start <ваш_email>");
        } else {
            sendText(chatId, "Вы не были подписаны.");
        }
    }

    private void handleStatus(Long chatId) {
        Optional<TelegramSubscription> sub = subscriptionRepo.findByChatId(chatId);
        if (sub.isPresent() && sub.get().isActive()) {
            TelegramSubscription s = sub.get();
            sendText(chatId,
                    "✅ *Статус: активна*\n" +
                            "Email: `" + s.getUsername() + "`\n" +
                            "Подключено: " + s.getCreatedAt().toLocalDate());
        } else {
            sendText(chatId, "❌ Подписка неактивна. Введите /start для подключения.");
        }
    }

    private void handleHelp(Long chatId) {
        sendText(chatId,
                "📖 *SEC Notify — помощь*\n\n" +
                        "/start <email> — подключить уведомления\n" +
                        "/stop — отключить уведомления\n" +
                        "/status — проверить статус\n" +
                        "/help — эта справка");
    }

    public void sendText(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setParseMode("Markdown");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Failed to send Telegram message to chatId {}: {}", chatId, e.getMessage());
        }
    }
}
