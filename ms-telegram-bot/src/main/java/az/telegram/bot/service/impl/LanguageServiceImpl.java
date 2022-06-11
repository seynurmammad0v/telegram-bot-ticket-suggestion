package az.telegram.bot.service.impl;

import az.telegram.bot.dao.Language;
import az.telegram.bot.dao.repository.LanguageRepository;
import az.telegram.bot.service.LanguageService;
import az.telegram.bot.service.SessionService;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final SessionService sessionService;
    private final LanguageRepository languageRepository;


    public LanguageServiceImpl(SessionService sessionService, LanguageRepository languageRepository) {
        this.sessionService = sessionService;
        this.languageRepository = languageRepository;
    }


    @Override
    public Language getUserLanguage(Long userId) {
        return languageRepository.getById(sessionService.getSessionLanguage(userId));
    }
}
