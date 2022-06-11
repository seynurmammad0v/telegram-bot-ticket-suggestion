package az.telegram.bot.model;

import lombok.Builder;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Builder
public class Bot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUsername;
    private String botToken;
    private String botPath;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public <T extends Serializable, Method extends BotApiMethod<T>> T executeMsg(Method method) {
        try {
            return this.execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
