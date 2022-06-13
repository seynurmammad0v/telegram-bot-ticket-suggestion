package az.telegram.bot.scheduler;

import az.telegram.bot.dao.Session;
import az.telegram.bot.model.enums.Status;
import az.telegram.bot.service.ListenerService;
import az.telegram.bot.service.SessionService;
import groovy.util.logging.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SessionScheduler {

    private final SessionService sessionService;
    private final ListenerService listenerService;

    public SessionScheduler(SessionService sessionService, ListenerService listenerService) {
        this.sessionService = sessionService;
        this.listenerService = listenerService;
    }

    @Scheduled(cron = "${cron.expiredTimeExpression}", zone = "Asia/Baku")
    public void expiredSessions() {
        List<Session> sessions = sessionService.getExpiredSessionsInStatus(Status.WAITING_REPLY_FOR_OFFER);
        for (Session s : sessions) {
            listenerService.sendExpiredNotification(s);
        }
    }
}