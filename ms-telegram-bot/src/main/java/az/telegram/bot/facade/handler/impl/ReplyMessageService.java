package az.telegram.bot.facade.handler.impl;

import az.telegram.bot.dao.AgencyOffer;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.repository.AgencyOfferRepository;
import az.telegram.bot.exceptions.OfferShouldBeRepliedException;
import az.telegram.bot.exceptions.StartBeforeException;
import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.model.enums.StaticStates;
import az.telegram.bot.service.MessageCreatorService;
import az.telegram.bot.service.SessionService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
@Qualifier("reply")
public class ReplyMessageService implements MessageService {
    private final MessageCreatorService msgCreatorService;
    private final SessionService sessionService;
    private final AgencyOfferRepository agencyOfferRepository;
    private final MessageService inputMessageService;

    public ReplyMessageService(MessageCreatorService msgCreatorService,
                               SessionService sessionService,
                               AgencyOfferRepository agencyOfferRepository,
                               @Qualifier("input") MessageService inputMessageService) {
        this.msgCreatorService = msgCreatorService;
        this.sessionService = sessionService;
        this.agencyOfferRepository = agencyOfferRepository;
        this.inputMessageService = inputMessageService;
    }

    @Override
    public SendMessage handle(Message msg, boolean isCommand) {
        Session session = sessionService.getSessionIfExist(msg.getFrom().getId());
        if (session != null) {
            AgencyOffer offer = agencyOfferRepository.getByMessageIdAndSessionId(msg.getReplyToMessage().getMessageId(), session.getId());
            if (offer != null) {
                offer.setAccepted(true);
                agencyOfferRepository.save(offer);
                if (Strings.isNullOrEmpty(offer.getPhoneNumber())) {
                    sessionService.setQuestionByState(offer.getSessionId(), StaticStates.REPLY_START);
                    offer.setFirstName(msg.getFrom().getFirstName());
                    offer.setLastName(msg.getFrom().getLastName());
                    offer.setUsername(msg.getFrom().getUserName());
                    return inputMessageService.handle(msg, true);
                } else {
                    sessionService.setQuestionByState(offer.getSessionId(), StaticStates.REPLY_END);
                    msg.setText(offer.getPhoneNumber());
                    agencyOfferRepository.save(offer);
                    return inputMessageService.handle(msg, true);
                }
            } else {
                return msgCreatorService.createError(msg.getChatId(),
                        new OfferShouldBeRepliedException(),
                        sessionService.getSessionLanguage(msg.getFrom().getId()));
            }
        } else {
            return msgCreatorService.createError(msg.getChatId(),
                    new StartBeforeException(),
                    sessionService.getSessionLanguage(msg.getFrom().getId()));
        }

    }


}
