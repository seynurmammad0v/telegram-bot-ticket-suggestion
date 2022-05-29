package az.telegram.bot.service.impl;

import az.telegram.bot.service.MessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Qualifier("contact")
public class ContactService implements MessageService {

    @Override
    public SendMessage handle(Message message, boolean isCommand) {
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("data")
                .build();
    }
}
