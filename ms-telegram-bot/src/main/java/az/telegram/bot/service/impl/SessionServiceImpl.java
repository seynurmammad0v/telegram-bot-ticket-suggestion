package az.telegram.bot.service.impl;

import az.telegram.bot.dao.*;
import az.telegram.bot.dao.repository.*;
import az.telegram.bot.model.enums.Status;
import az.telegram.bot.service.SessionService;
import org.springframework.stereotype.Service;


@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final LanguageRepository languageRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, UserRepository userRepository, QuestionRepository questionRepository, AnswerRepository answerRepository, LanguageRepository languageRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public Session createSession(Long userId, Long chatId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null) {
            user = User.builder()
                    .userId(userId)
                    .build();
        }
        Answer answer = Answer.builder()
                .question(questionRepository.getFirstQuestion())
                .build();
        Session session = Session.builder()
                .user(user)
                .status(Status.IN_PROGRESS)
                .chatId(chatId)
                .build();
        answer.setSession(session);
        userRepository.save(user);
        sessionRepository.save(session);
        answerRepository.save(answer);
        return session;
    }

    @Override
    public Session getSessionIfExist(Long userId) {
        return sessionRepository.getByUserId(userId, Status.IN_PROGRESS);
    }

    @Override
    public void deactivateSession(Long userId) {
        sessionRepository.setStatusByUserId(userId, Status.INACTIVE);
    }

    @Override
    public Long getSessionLanguage(Long userId) {
        Session session = sessionRepository.getByUserId(userId, Status.IN_PROGRESS);
        return (session == null) ? 4L : session.getLangId();
    }

    @Override
    public void setLanguage(Long userId, String lang) {
        Session session = sessionRepository.getByUserId(userId, Status.IN_PROGRESS);
        session.setLangId(languageRepository.getLanguageByLang(lang).getId());
        sessionRepository.save(session);
    }

    @Override
    public void setAnswer(Long userId, String text) {
        Session session = sessionRepository.getByUserId(userId, Status.IN_PROGRESS);
        Answer answer = answerRepository.findFirstBySession_IdOrderByIdDesc(session.getId());
        answer.setText(text);
        answerRepository.save(answer);
    }

    @Override
    public void createNextAnswerForQuestion(Long userId, Question question) {
        Session session = sessionRepository.getByUserId(userId, Status.IN_PROGRESS);
        Answer answer = Answer.builder()
                .question(question)
                .session(session)
                .build();
        answerRepository.save(answer);
    }

}
