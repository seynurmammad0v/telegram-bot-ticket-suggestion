package az.telegram.bot.service;

import az.telegram.bot.dao.Question;
import org.aspectj.weaver.patterns.TypePatternQuestions;

public interface QuestionService {
    Question getLastQuestion(Long userId);
}
