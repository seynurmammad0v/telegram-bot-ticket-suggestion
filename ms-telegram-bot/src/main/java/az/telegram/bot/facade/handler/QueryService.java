package az.telegram.bot.facade.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface QueryService {
    BotApiMethod<?> handle(CallbackQuery buttonQuery);
}