package az.telegram.bot.utils;

import az.telegram.bot.dao.Action;
import az.telegram.bot.dao.ActionTranslate;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.QuestionTranslate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TranslateUtil {
    public List<ActionTranslate> getActionsTranslate(Set<Action> actions, Long langId) {
        return actions.stream()
                .flatMap(a -> a.getActionTranslates().stream()
                        .filter(q -> Objects.equals(q.getLanguage().getId(), langId)))
                .collect(Collectors.toList());
    }

    public String getQuestionTranslate(Question question, Long langId) {
        return question.getQuestionTranslates()
                .stream()
                .filter(q -> Objects.equals(q.getLanguage().getId(), langId))
                .map(QuestionTranslate::getContext)
                .collect(Collectors.joining("\n"));
    }
}
