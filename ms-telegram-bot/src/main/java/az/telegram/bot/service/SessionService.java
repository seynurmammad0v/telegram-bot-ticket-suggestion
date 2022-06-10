package az.telegram.bot.service;

import az.telegram.bot.dao.Session;

public interface SessionService {
    Session createSession(Long userId, Long chatId);

    Boolean haveSession(Long userId);

    void deactivateSession(Long userId);

    Long getSessionLanguage(Long userId);
}
