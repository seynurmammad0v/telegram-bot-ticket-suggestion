package az.telegram.bot.service;

import az.telegram.bot.dao.Language;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;
import az.telegram.bot.exceptions.CustomException;
import az.telegram.bot.model.enums.ActionType;
import org.joda.time.LocalDate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;


public interface MessageCreatorService {
    SendMessage createNextBtn(Session session, Question nextQuestion, Long langId);

    EditMessageText updateNextBtn(Session session, Question nextQuestion, Long langId);

    SendMessage simpleQuestionMessage(Long chatId, Question question, Long langId);

    SendMessage createCalendar(Long chatId, Question question, Language language);

    EditMessageReplyMarkup updateCalendar(Message message, Language language, LocalDate localDate) ;

    String questionGenerator(Question question, Long langId);

    SendMessage msgWithRepKeyboard(Long chatId, Question question, Long langId);

    SendMessage msgWithInlKeyboard(Long chatId, Question question, Long langId);

    SendMessage createMsgWithData(Long chatId, Long userId, String data);

    SendPhoto createPhoto(Long chatId, InputFile inputFile);

    SendMessage createError(Long chatId, CustomException exception, Long langId);

    SendMessage createNotify(Long chatId, CustomException exception, Long langId);

    DeleteMessage deleteMessage(Long chatId, Integer messageId);

    EditMessageText editCalendarMessage(Question question, Message message, Long langId);

    SendMessage getMessageByAction(Question question, Language language, ActionType actionType, Long chatId);
}
