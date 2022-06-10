package az.telegram.bot.service.impl;

import az.telegram.bot.dao.Answer;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.User;
import az.telegram.bot.dao.repository.AnswerRepository;
import az.telegram.bot.dao.repository.QuestionRepository;
import az.telegram.bot.dao.repository.SessionRepository;
import az.telegram.bot.dao.repository.UserRepository;
import az.telegram.bot.model.enums.Status;
import az.telegram.bot.service.SessionService;
import org.springframework.stereotype.Service;


@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public SessionServiceImpl(SessionRepository sessionRepository,
                              UserRepository userRepository,
                              QuestionRepository questionRepository,
                              AnswerRepository answerRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
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
    public Boolean haveSession(Long userId) {
        Session session = sessionRepository.getByUserId(userId, Status.IN_PROGRESS);
        return session != null;
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

}
