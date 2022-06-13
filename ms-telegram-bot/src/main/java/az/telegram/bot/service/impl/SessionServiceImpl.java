package az.telegram.bot.service.impl;

import az.telegram.bot.dao.Answer;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.User;
import az.telegram.bot.dao.repository.AnswerRepository;
import az.telegram.bot.dao.repository.LanguageRepository;
import az.telegram.bot.dao.repository.QuestionRepository;
import az.telegram.bot.dao.repository.SessionRepository;
import az.telegram.bot.dao.repository.UserRepository;
import az.telegram.bot.model.enums.StaticStates;
import az.telegram.bot.model.enums.Status;
import az.telegram.bot.service.SessionService;
import org.springframework.stereotype.Service;

import java.util.List;


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
            user = User.builder().userId(userId).build();
        }
        Answer answer = Answer.builder().question(questionRepository.getFirstQuestion()).build();
        Session session = Session.builder().user(user).status(Status.IN_PROGRESS).chatId(chatId).build();
        answer.setSession(session);
        userRepository.save(user);
        sessionRepository.save(session);
        answerRepository.save(answer);
        return session;
    }

    @Override
    public Session getSessionIfExist(Long userId) {
        return sessionRepository.getByUserIdAndNotInStatus(userId, Status.INACTIVE);
    }
    @Override
    public Session getSessionById(Long sessionId) {
        return sessionRepository.getBySessionIdAndNotInStatus(sessionId, Status.INACTIVE);
    }

    @Override
    public void changeSessionStatus(Status status, Long sessionId) {
        sessionRepository.setStatusBySessionId(status, sessionId);
    }

    @Override
    public Long getSessionLanguage(Long userId) {
        Session session = sessionRepository.getByUserIdAndNotInStatus(userId, Status.INACTIVE);
        return (session == null) ? 4L : session.getLangId();
    }

    @Override
    public void setLanguage(Long userId, String lang) {
        Session session = sessionRepository.getByUserIdAndNotInStatus(userId, Status.INACTIVE);
        session.setLangId(languageRepository.getLanguageByLang(lang).getId());
        sessionRepository.save(session);
    }

    @Override
    public void setAnswer(Long userId, String text) {
        Session session = sessionRepository.getByUserIdAndNotInStatus(userId, Status.INACTIVE);
        Answer answer = answerRepository.findFirstBySession_IdOrderByIdDesc(session.getId());
        answer.setText(text);
        answerRepository.save(answer);
    }

    @Override
    public void createNextAnswerForQuestion(Long userId, Question question) {
        Session session = sessionRepository.getByUserIdAndNotInStatus(userId, Status.INACTIVE);
        Answer answer = Answer.builder().question(question).session(session).build();
        answerRepository.save(answer);
    }

    @Override
    public void setQuestionByState(Long sessionId, StaticStates state) {
        Session session = sessionRepository.getById(sessionId);
        Answer answer = Answer.builder().question(questionRepository.getByState(state.toString())).build();
        answer.setSession(session);
        answerRepository.save(answer);
    }

    @Override
    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    @Override
    public List<Session> getExpiredSessionsInStatus(Status status) {
        return sessionRepository.getExpiredSessionsInStatus(status);
    }


}
