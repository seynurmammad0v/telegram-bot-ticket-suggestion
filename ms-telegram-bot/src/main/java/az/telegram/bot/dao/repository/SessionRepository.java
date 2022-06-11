package az.telegram.bot.dao.repository;

import az.telegram.bot.dao.Session;
import az.telegram.bot.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Modifying
    @Transactional
    @Query("update Session t set t.status=:status where t.id=:sessionId")
    void setStatusBySessionId(Status status, Long sessionId);

    @Query("select t from Session t where t.user.userId=:userId and not t.status=:status")
    Session getByUserIdAndNotInStatus(Long userId, Status status);
//todo
//    @Query("SELECT t FROM Session t WHERE t.isWaitingAnswer=true and t.expiredAt < CURRENT_TIMESTAMP and t.status=true")
//    List<Session> getNotAnsweredSessions();

}
