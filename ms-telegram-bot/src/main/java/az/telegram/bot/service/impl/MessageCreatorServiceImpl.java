package az.telegram.bot.service.impl;

import az.telegram.bot.dao.Action;
import az.telegram.bot.dao.Language;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;
import az.telegram.bot.exceptions.CustomException;
import az.telegram.bot.model.enums.ActionType;
import az.telegram.bot.service.MessageCreatorService;
import az.telegram.bot.utils.ButtonsUtil;
import az.telegram.bot.utils.CalendarUtil;
import az.telegram.bot.utils.TranslateUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.Set;

import org.joda.time.LocalDate;


@Service
public class MessageCreatorServiceImpl implements MessageCreatorService {
    private final ButtonsUtil buttonsUtil;

    private final CalendarUtil calendarUtil;

    private final TranslateUtil translateUtil;

    public MessageCreatorServiceImpl(ButtonsUtil buttonsUtil, CalendarUtil calendarUtil, TranslateUtil translateUtil) {
        this.buttonsUtil = buttonsUtil;
        this.calendarUtil = calendarUtil;
        this.translateUtil = translateUtil;
    }

    @Override
    public String questionGenerator(Question question, Long langId) {
        return translateUtil.getQuestionTranslate(question, langId);
    }

    @Override
    public SendMessage createNextBtn(Session session, Question nextQuestion, Long langId) {
        return buttonsUtil.buttonMessage(
                createInlMarkup(nextQuestion.getActions(), langId),
                session.getChatId().toString(),
                getNextBtnText(session, nextQuestion, langId)
        );
    }

    @Override
    public EditMessageText updateNextBtn(Session session, Question nextQuestion, Long langId) {
        return EditMessageText.builder()
                .chatId(session.getChatId().toString())
                .messageId(session.getNextMessageId())
                .text(getNextBtnText(session, nextQuestion, langId))
                .replyMarkup(createInlMarkup(nextQuestion.getActions(), langId))
                .build();
    }

    public String getNextBtnText(Session session, Question nextQuestion, Long langId) {
        return String.format(
                questionGenerator(nextQuestion, langId),
                session.getOffersCount() - session.getSentCount()
        );
    }

    @Override
    public SendMessage simpleQuestionMessage(Long chatId, Question question, Long langId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(questionGenerator(question, langId))
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build();
    }

    @Override
    public SendMessage createCalendar(Long chatId, Question question, Language language) {
        return buttonsUtil.buttonMessage(
                calendarUtil.generateCalendar(LocalDate.now(), language),
                chatId.toString(),
                questionGenerator(question, language.getId())
        );
    }

    @Override
    public EditMessageReplyMarkup updateCalendar(Message message, Language language, LocalDate localDate) {
        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .replyMarkup(calendarUtil.generateCalendar(localDate, language))
                .messageId(message.getMessageId())
                .build();

    }

    @Override
    public SendMessage msgWithInlKeyboard(Long chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                createInlMarkup(question.getActions(), langId),
                chatId.toString(),
                questionGenerator(question, langId)
        );
    }

    @Override
    public SendMessage msgWithRepKeyboard(Long chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                createRepMarkup(question.getActions(), langId),
                chatId.toString(),
                questionGenerator(question, langId)
        );
    }

    @Override
    public SendMessage createMsgWithData(Long chatId, Long userId, String data) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(data)
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build();
    }

    @Override
    public SendPhoto createPhoto(Long chatId, InputFile inputFile) {
        return SendPhoto.builder()
                .chatId(chatId.toString())
                .photo(inputFile)
                .build();
    }

    @Override
    public SendMessage createError(Long chatId, CustomException exception, Long langId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(exception.getLocalizedMessage(langId))
                .build();
    }

    @Override
    public SendMessage createNotify(Long chatId, CustomException exception, Long langId) {
        return SendMessage.builder()
                .chatId(chatId.toString())
                .text(exception.getLocalizedMessage(langId))
                .replyMarkup(buttonsUtil.removeReplyKeyboard())
                .build();
    }

    @Override
    public EditMessageText editCalendarMessage(Question question, Message message, Long langId) {
        return EditMessageText.builder()
                .chatId(message.getChatId().toString())
                .text(
                        questionGenerator(question, langId) + "\n" + message.getText()
                )
                .messageId(message.getMessageId())
                .replyMarkup(new InlineKeyboardMarkup(new ArrayList<>()))
                .build();
    }

    @Override
    public DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder().chatId(chatId.toString()).messageId(messageId).build();
    }

    @Override
    public SendMessage getMessageByAction(Question question,
                                          Language language, ActionType actionType, Long chatId) {
        switch (actionType) {
            case FREETEXT:
                return simpleQuestionMessage(chatId, question, language.getId());
            case BUTTON:
                return msgWithRepKeyboard(chatId, question, language.getId());
            case INLINE_BUTTON:
                return msgWithInlKeyboard(chatId, question, language.getId());
            case CALENDAR:
                return createCalendar(chatId, question, language);
            case BUTTON_CONTACT_INFO:
                return createBtnContactInfo(chatId.toString(), question, language.getId());
            default:
                return null;
        }
    }

    private SendMessage createBtnContactInfo(String chatId, Question question, Long langId) {
        return buttonsUtil.buttonMessage(
                createRepMarkup(question.getActions(), langId, true),
                chatId,
                questionGenerator(question, langId)
        );
    }

    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId, boolean contact_info) {
        return ReplyKeyboardMarkup.builder()
                .keyboard(
                        buttonsUtil.createRepKeyboard(
                                translateUtil.getActionsTranslate(actions, langId), contact_info
                        )
                )
                .resizeKeyboard(true)
                .selective(true)
                .oneTimeKeyboard(false)
                .build();
    }

    private ReplyKeyboardMarkup createRepMarkup(Set<Action> actions, Long langId) {
        return createRepMarkup(actions, langId, false);
    }

    private InlineKeyboardMarkup createInlMarkup(Set<Action> actions, Long langId) {
        return new InlineKeyboardMarkup(
                buttonsUtil.createInlKeyboard(
                        translateUtil.getActionsTranslate(actions, langId)
                )
        );
    }
}
