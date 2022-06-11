package az.telegram.bot.service;

import az.telegram.bot.dao.Language;

public interface LanguageService {
    Language getUserLanguage(Long userId);

}
