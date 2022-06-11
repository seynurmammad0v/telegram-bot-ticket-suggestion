package az.telegram.bot.facade;

import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.facade.handler.QueryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Component
public class TelegramFacade {

    private final QueryService callbackQueryService;
    private final MessageService inputMessageService;
    private final MessageService commandHandler;
    private final MessageService replyHandler;
    private final MessageService contactHandler;

    public TelegramFacade(
            @Qualifier("input") MessageService inputMessageService,
            @Qualifier("command") MessageService commandHandler,
            @Qualifier("reply") MessageService replyHandler,
            @Qualifier("contact") MessageService contactHandler,
            QueryService callbackQueryService) {
        this.inputMessageService = inputMessageService;
        this.callbackQueryService = callbackQueryService;
        this.commandHandler = commandHandler;
        this.replyHandler = replyHandler;
        this.contactHandler = contactHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) throws TelegramApiException, IOException {
        Message message = update.getMessage();
        if (update.hasCallbackQuery()) {
            return callbackQueryService.handle(update.getCallbackQuery());
        } else if (message != null) {
            if (message.hasContact()) {
                return contactHandler.handle(message, false);
            } else if (message.getReplyToMessage() != null) {
                return replyHandler.handle(message, true);
            } else if (isCommand(message)) {
                return commandHandler.handle(message, true);
            } else if (message.hasText()) {
                return inputMessageService.handle(message, false);
            }
        }
        return null;
    }

    public boolean isCommand(Message message) {
        if (message.hasText()) {
            return message.getText().startsWith("/");
        }
        return false;
    }

}