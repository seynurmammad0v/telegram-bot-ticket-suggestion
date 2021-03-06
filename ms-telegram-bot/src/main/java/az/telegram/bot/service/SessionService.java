package az.telegram.bot.service;

import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;
import az.telegram.bot.model.enums.StaticStates;
import az.telegram.bot.model.enums.Status;

import java.util.List;

public interface SessionService {
    Session createSession(Long userId, Long chatId);

    Session getSessionIfExist(Long userId);

    Session getSessionById(Long sessionId);

    void changeSessionStatus(Status status, Long sessionId);

    Long getSessionLanguage(Long userId);

    void setLanguage(Long userId, String lang);

    void setAnswer(Long userId, String text);

    void createNextAnswerForQuestion(Long userId, Question question);

    void setQuestionByState(Long sessionId, StaticStates state);

    void saveSession(Session session);

    List<Session> getExpiredSessionsInStatus(Status status);
}
