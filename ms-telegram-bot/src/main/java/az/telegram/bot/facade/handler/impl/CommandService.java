package az.telegram.bot.facade.handler.impl;

import az.telegram.bot.dao.Session;
import az.telegram.bot.exceptions.StartBeforeStopException;
import az.telegram.bot.exceptions.StopBeforeException;
import az.telegram.bot.exceptions.StopNotifyException;
import az.telegram.bot.exceptions.UnknownCommandException;
import az.telegram.bot.model.enums.CommandType;
import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.model.enums.Status;
import az.telegram.bot.service.MessageCreatorService;
import az.telegram.bot.service.SessionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Service
@Qualifier("command")
public class CommandService implements MessageService {

    private final MessageCreatorService msgCreatorService;
    private final SessionService sessionService;
    private final MessageService inputMessageService;

    public CommandService(MessageCreatorService msgCreatorService,
                          SessionService sessionService,
                          @Qualifier("input") MessageService inputMessageService) {
        this.msgCreatorService = msgCreatorService;
        this.sessionService = sessionService;
        this.inputMessageService = inputMessageService;
    }

    @Override
    public SendMessage handle(Message message, boolean isCommand) {
        CommandType commandType = CommandType.valueOfCommand(message.getText());
        switch (commandType) {
            case START:
                return startCommand(message);
            case STOP:
                return stopCommand(message);
            default:
                return msgCreatorService.createError(
                        message.getChatId(),
                        new UnknownCommandException(),
                        sessionService.getSessionLanguage(message.getFrom().getId()));
        }
    }

    private SendMessage startCommand(Message message) {
        if (sessionService.getSessionIfExist(message.getFrom().getId()) == null) {
            sessionService.createSession(message.getFrom().getId(), message.getChatId());
            return inputMessageService.handle(message, true);
        } else {
            return msgCreatorService.createError(message.getChatId(),
                    new StopBeforeException(),
                    sessionService.getSessionLanguage(message.getFrom().getId()));
        }
    }

    public SendMessage stopCommand(Message msg) {
        Session session = sessionService.getSessionIfExist(msg.getFrom().getId());
        if (session != null) {
            sessionService.changeSessionStatus(Status.INACTIVE, session.getId());
            return msgCreatorService.createNotify(msg.getChatId(),
                    new StopNotifyException(),
                    session.getLangId());
        } else {
            return msgCreatorService.createError(msg.getChatId(),
                    new StartBeforeStopException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId()));
        }
    }


}
