package az.telegram.bot.dao.repository;

import az.telegram.bot.dao.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question getByState(String state);

    @Query("select a.question from Action a where a.nextQuestion is not null and a.question.state is null ")
    Question getFirstQuestion();

}

