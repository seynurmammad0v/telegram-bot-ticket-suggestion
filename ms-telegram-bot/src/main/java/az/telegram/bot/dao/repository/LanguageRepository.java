package az.telegram.bot.dao.repository;

import az.telegram.bot.dao.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language getLanguageByLang(String name);
}
