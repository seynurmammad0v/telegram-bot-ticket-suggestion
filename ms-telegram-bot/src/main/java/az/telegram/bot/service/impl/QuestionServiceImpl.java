package az.telegram.bot.service.impl;

import az.telegram.bot.dao.Answer;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.repository.AnswerRepository;
import az.telegram.bot.dao.repository.QuestionRepository;
import az.telegram.bot.service.QuestionService;
import az.telegram.bot.service.SessionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SessionService sessionService;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, SessionService sessionService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.sessionService = sessionService;
    }

    @Override
    public Question getLastQuestion(Long userId) {
        Session session = sessionService.getSessionIfExist(userId);
        if (session == null) {
            return null;
        }
        Answer lastAnswer = answerRepository.findFirstBySession_IdOrderByIdDesc(session.getId());
        return questionRepository.getById(lastAnswer.getQuestion().getId());
    }
}
