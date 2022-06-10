package az.telegram.bot.dao.repository;

import az.telegram.bot.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUserId(Long userID);
}
