package az.telegram.bot.facade.handler.impl;

import az.telegram.bot.dao.Action;
import az.telegram.bot.dao.Question;
import az.telegram.bot.exceptions.IncorrectAnswerException;
import az.telegram.bot.exceptions.OfferShouldBeRepliedException;
import az.telegram.bot.exceptions.StartBeforeException;
import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.model.Bot;
import az.telegram.bot.model.enums.ActionType;
import az.telegram.bot.model.enums.StaticStates;
import az.telegram.bot.service.LanguageService;
import az.telegram.bot.service.MessageCreatorService;
import az.telegram.bot.service.QuestionService;
import az.telegram.bot.service.SessionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;


@Component
@Qualifier("input")
public class InputMessageService implements MessageService {

    private final MessageCreatorService msgCreatorService;
    private final SessionService sessionService;
    private final QuestionService questionService;
    private final LanguageService languageService;
    private final Bot bot;

    public InputMessageService(MessageCreatorService msgCreatorService, SessionService sessionService, QuestionService questionService, LanguageService languageService, Bot bot) {
        this.msgCreatorService = msgCreatorService;
        this.sessionService = sessionService;
        this.questionService = questionService;
        this.languageService = languageService;
        this.bot = bot;
    }

    @Override
    public SendMessage handle(Message msg, boolean command) {
        Question currentQ = questionService.getLastQuestion(msg.getFrom().getId());
        SendMessage sendMessage = checkQuestionStatus(currentQ, msg);
        if (sendMessage != null) {
            return sendMessage;
        }
        if (!command) {
            regexAnswerType(currentQ, msg);
            currentQ = questionService.getLastQuestion(msg.getFrom().getId());
        }
        return getMessage(currentQ, msg);
    }

    private SendMessage checkQuestionStatus(Question currentQuestion, Message msg) {
        if (currentQuestion == null) {
            return msgCreatorService.createError(msg.getChatId(),
                    new StartBeforeException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId()));
        } else if (Objects.equals(currentQuestion.getState(), StaticStates.END.toString())) {
            return msgCreatorService.createError(msg.getChatId(),
                    new OfferShouldBeRepliedException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId()));
        } else if (isCalendarQuestion(currentQuestion, msg.getText())) {
            return msgCreatorService.createError(msg.getChatId(),
                    new IncorrectAnswerException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId()));
        }
        return null;
    }

    public boolean isCalendarQuestion(Question currentQuestion, String text) {
        ActionType actionType = currentQuestion.getActions().stream()
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getType();
        return actionType == ActionType.CALENDAR && !Pattern.matches(currentQuestion.getRegex(),text);
    }

    private void regexAnswerType(Question question, Message msg) {
        Action action = question.getActions().iterator().next();
        if (action.getType() == ActionType.FREETEXT) {
            regexFreeText(question, msg, action);
        } else if (action.getType() == ActionType.BUTTON) {
            regexButton(question, msg);
        } else if (action.getType() == ActionType.BUTTON_CONTACT_INFO) {
            setAnswerAndNextQuestion(question, msg, action);
        } else if (action.getType() == ActionType.CALENDAR) {
            bot.executeMsg(msgCreatorService.editCalendarMessage(
                    question,
                    msg,
                    sessionService.getSessionLanguage(msg.getFrom().getId()))
            );
            setAnswerAndNextQuestion(question, msg, action);
        }
    }

    private void regexButton(Question question, Message msg) {
        Optional<Action> filteredAnswer = question.getActions().stream()
                .filter(a -> a.getActionTranslates().stream()
                        .anyMatch(t -> t.getContext().equals(msg.getText())))
                .findFirst();

        if (filteredAnswer.isPresent()) {
            setAnswerAndNextQuestion(question, msg, filteredAnswer.get());
        } else if (question.getState() != null) {
            bot.executeMsg(msgCreatorService.createError(msg.getChatId(),
                    new IncorrectAnswerException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId())));
        }
    }


    private void regexFreeText(Question question, Message msg, Action action) {
        if (Pattern.matches(question.getRegex(), msg.getText())) {
            setAnswerAndNextQuestion(question, msg, action);
        } else {
            bot.executeMsg(msgCreatorService.createError(msg.getChatId(),
                    new IncorrectAnswerException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId())));
        }
    }

    private void setAnswerAndNextQuestion(Question question, Message msg, Action filteredAnswer) {
        if (question.getState() == null) {
            sessionService.setLanguage(msg.getFrom().getId(), msg.getText());
            sessionService.setAnswer(msg.getFrom().getId(), "language");
        }
        if (filteredAnswer.getKeyword() != null) {
            sessionService.setAnswer(msg.getFrom().getId(), filteredAnswer.getKeyword());
        } else {
            sessionService.setAnswer(msg.getFrom().getId(), msg.getText());
        }
        sessionService.createNextAnswerForQuestion(msg.getFrom().getId(), filteredAnswer.getNextQuestion());
    }


    public SendMessage getMessage(Question question, Message msg) {
        ActionType actionType = question.getActions().stream()
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getType();

        sendOfferOrAnswers(question, msg.getText());

        return msgCreatorService.getMessageByAction(question,
                languageService.getUserLanguage(msg.getFrom().getId()),
                actionType,
                msg.getChatId());
    }


    private void sendOfferOrAnswers(Question question, String userAnswer) {
        boolean end = Objects.equals(question.getState(), StaticStates.REPLY_END.toString());
        Optional<Action> questionAction = question.getActions().stream().findFirst();
        if (questionAction.isPresent()) {
            if (questionAction.get().getNextQuestion() == null && !end) {
                sendCollectedData();
            }
        }
        if (end) {
            acceptOffer(userAnswer);
        }
    }


    private void sendCollectedData() {
//        template.convertAndSend(RabbitMQConfig.exchange,
//                RabbitMQConfig.sent,
//                dataCache.getUserData(userId));
//        dataCache.setQuestion(userId, Question.builder().state(StaticStates.END.toString()).build());
//        dataCache.clearData(userId);
    }

    private void acceptOffer(String phoneNumber) {
//        AcceptedOffer offer = acceptedOfferRepository.findById(userId);
//        offer.setPhoneNumber(phoneNumber);
//        acceptedOfferRepository.save(userId, offer);
//        template.convertAndSend(RabbitMQConfig.exchange, RabbitMQConfig.accepted, offer);
//        dataCache.setQuestion(userId, Question.builder().state(StaticStates.END.toString()).build());
//        dataCache.clearData(userId);
    }
}
