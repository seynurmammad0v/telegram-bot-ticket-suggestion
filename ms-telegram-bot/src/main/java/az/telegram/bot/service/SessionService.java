package az.telegram.bot.service;

import az.telegram.bot.dao.Action;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;

public interface SessionService {
    Session createSession(Long userId, Long chatId);

    Session getSessionIfExist(Long userId);

    void deactivateSession(Long userId);

    Long getSessionLanguage(Long userId);

    void setLanguage(Long userId, String lang);

    void setAnswer(Long userId, String text);

    void createNextAnswerForQuestion(Long userId, Question question);
}
