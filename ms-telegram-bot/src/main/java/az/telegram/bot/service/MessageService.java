package az.telegram.bot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageService {
    SendMessage handle(Message message, boolean isCommand);
}
