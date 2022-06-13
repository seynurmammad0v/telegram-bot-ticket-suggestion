package az.telegram.bot.facade.handler.impl;

import az.telegram.bot.dao.Question;
import az.telegram.bot.exceptions.BeforeCurrentDateException;
import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.facade.handler.QueryService;
import az.telegram.bot.model.Bot;
import az.telegram.bot.model.enums.ActionType;
import az.telegram.bot.model.enums.StaticStates;
import az.telegram.bot.service.*;
import az.telegram.bot.utils.CalendarUtil;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class CallbackQueryService implements QueryService {
    private final QuestionService questionService;
    private final Bot bot;
    private final MessageCreatorService msgCreatorService;
    private final LanguageService languageService;
    private final SessionService sessionService;
    private final MessageService inputMessageService;

    private final ListenerService listenerService;

    public CallbackQueryService(QuestionService questionService,
                                Bot bot,
                                MessageCreatorService msgCreatorService,
                                LanguageService languageService,
                                SessionService sessionService,
                                @Qualifier("input") MessageService inputMessageService,
                                ListenerService listenerService) {
        this.questionService = questionService;
        this.bot = bot;
        this.msgCreatorService = msgCreatorService;
        this.languageService = languageService;
        this.sessionService = sessionService;
        this.inputMessageService = inputMessageService;
        this.listenerService = listenerService;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery msg) {
        BotApiMethod<?> message = regexInlineButton(msg);
        if (message != null) {
            return message;
        } else {
            message = queryByQuestion(msg);
        }
        return message;
    }

    private BotApiMethod<?> regexInlineButton(CallbackQuery msg) {
        if (Objects.equals(msg.getData(), StaticStates.NEXT.toString())) {
            listenerService.sendNextPhotos(msg.getFrom().getId());
            return msgCreatorService.deleteMessage(msg.getMessage().getChatId(), msg.getMessage().getMessageId());
        }
        return null;
    }


    private BotApiMethod<?> queryByQuestion(CallbackQuery msg) {
        Question question = questionService.getLastQuestion(msg.getFrom().getId());
        ActionType actionType = getActionType(msg, question);
        if (actionType == null) return null;
        if (actionType == ActionType.CALENDAR) {
            return regexCalendar(msg, question);
        }
        return null;
    }

    private ActionType getActionType(CallbackQuery msg, Question question) {
        ActionType actionType;
        try {
            actionType = question.getActions().stream()
                    .findFirst()
                    .orElseThrow(RuntimeException::new)
                    .getType();
        } catch (Exception e) {
            bot.executeMsg(msgCreatorService.deleteMessage(
                    msg.getMessage().getChatId(),
                    msg.getMessage().getMessageId()
            ));
            return null;
        }
        return actionType;
    }

    private BotApiMethod<?> regexCalendar(CallbackQuery msg, Question question) {
        if (msg.getData().equals(CalendarUtil.IGNORE))
            return null;
        else if (Pattern.matches(question.getRegex(), msg.getData())) {
            return setCalendarAnswer(msg);
        } else {
            return msgCreatorService.updateCalendar(msg.getMessage(),
                    languageService.getUserLanguage(msg.getFrom().getId()),
                    LocalDate.parse(msg.getData() + "-" + LocalDate.now().getDayOfMonth())
            );
        }
    }

    private SendMessage setCalendarAnswer(CallbackQuery msg) {
        LocalDate date = LocalDate.parse(msg.getData());
        if (date.isBefore(LocalDate.now())) {
            return msgCreatorService.createError(
                    msg.getMessage().getChatId(),
                    new BeforeCurrentDateException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId()));
        }

        msg.getMessage().setText(msg.getData());
        msg.getMessage().setFrom(msg.getFrom());
        return inputMessageService.handle(msg.getMessage(), false);
    }

}
