package az.telegram.bot.utils;

import az.telegram.bot.dao.Session;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class MessageFakerUtil {
    public Message fakeStop(Session session, String chatId) {
        Message message = new Message();
        User user = new User();
        user.setId(session.getUser().getUserId());
        Chat chat = new Chat();
        chat.setId(Long.valueOf(chatId));
        message.setText("/stop");
        message.setFrom(user);
        message.setChat(chat);
        return message;
    }
}
