package az.telegram.bot.facade.handler.impl;

import az.telegram.bot.dao.AgencyOffer;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.repository.AgencyOfferRepository;
import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.service.SessionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Qualifier("contact")
public class ContactService implements MessageService {
    private final SessionService sessionService;
    private final MessageService inputMessageService;
    private final AgencyOfferRepository agencyOfferRepository;

    public ContactService(SessionService sessionService,
                          @Qualifier("input") MessageService inputMessageService,
                          AgencyOfferRepository agencyOfferRepository) {
        this.sessionService = sessionService;
        this.inputMessageService = inputMessageService;
        this.agencyOfferRepository = agencyOfferRepository;
    }

    @Override
    public SendMessage handle(Message msg, boolean isCommand) {
        Session session = sessionService.getSessionIfExist(msg.getFrom().getId());
        if (session != null) {
            session.setTelegramNumber(true);
            msg.setText(msg.getContact().getPhoneNumber());
            sessionService.saveSession(session);
            return inputMessageService.handle(msg, false);
        }
        return null;
    }
}
