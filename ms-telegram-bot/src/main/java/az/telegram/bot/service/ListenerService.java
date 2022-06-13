package az.telegram.bot.service;

import az.telegram.bot.dao.AgencyOffer;
import az.telegram.bot.dao.Session;

public interface ListenerService {
    void sendPhoto(AgencyOffer offer);

    void sendExpiredNotification(Session session);

    void sendNextPhotos(Long userId);
}
