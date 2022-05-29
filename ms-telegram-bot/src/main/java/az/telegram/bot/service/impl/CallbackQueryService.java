package az.telegram.bot.service.impl;

import az.telegram.bot.service.QueryService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Service
public class CallbackQueryService implements QueryService {
    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery) {
        return SendMessage.builder()
                .chatId(buttonQuery.getMessage().getChatId().toString())
                .text("data")
                .build();
    }
}
