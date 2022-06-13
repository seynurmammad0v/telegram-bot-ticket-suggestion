package az.telegram.bot.service.impl;

import az.telegram.bot.dao.AgencyOffer;
import az.telegram.bot.dao.Question;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.repository.AgencyOfferRepository;
import az.telegram.bot.dao.repository.QuestionRepository;
import az.telegram.bot.exceptions.NoOffersElseException;
import az.telegram.bot.exceptions.RequestWasStoppedException;
import az.telegram.bot.facade.handler.MessageService;
import az.telegram.bot.model.Bot;
import az.telegram.bot.model.enums.StaticStates;
import az.telegram.bot.model.enums.Status;
import az.telegram.bot.service.ListenerService;
import az.telegram.bot.service.MessageCreatorService;
import az.telegram.bot.service.SessionService;
import az.telegram.bot.utils.FileUtil;
import az.telegram.bot.utils.MessageFakerUtil;
import az.telegram.bot.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ListenerServiceImpl implements ListenerService {
    private final SessionService sessionService;

    private final Bot bot;

    private final FileUtil fileUtil;

    @Value("${offer.sentCount}")
    String MAX_OFFERS_COUNT;

    private final MessageCreatorService msgCreatorService;

    private final MessageFakerUtil fakerUtil;
    private final TimeUtil timeUtil;

    private final MessageService commandService;
    private final AgencyOfferRepository offerRepository;
    private final QuestionRepository questionRepository;

    public ListenerServiceImpl(SessionService sessionService,
                               Bot bot,
                               FileUtil fileUtil,
                               MessageCreatorService msgCreatorService,
                               MessageFakerUtil fakerUtil,
                               TimeUtil timeUtil,
                               @Qualifier("command") MessageService commandService,
                               AgencyOfferRepository offerRepository,
                               QuestionRepository questionRepository) {
        this.sessionService = sessionService;
        this.bot = bot;
        this.fileUtil = fileUtil;
        this.msgCreatorService = msgCreatorService;
        this.fakerUtil = fakerUtil;
        this.timeUtil = timeUtil;
        this.commandService = commandService;
        this.offerRepository = offerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public void sendPhoto(AgencyOffer offer) {
        Session session = sessionService.getSessionById(offer.getSessionId());
        session.setOffersCount(session.getOffersCount() + 1);
        sendActions(offer, session, fileUtil.byteArrToInputFile(offer.getFile()));
    }

    @Override
    public void sendExpiredNotification(Session session) {
        if (session.getOffersCount() > 0) {
            bot.executeMsg(
                    msgCreatorService.createNotify(
                            session.getChatId(),
                            new NoOffersElseException(),
                            session.getLangId())
            );
            session.setStatus(Status.WAITING_REPLY_FOR_OFFER);
            session.setExpiredAt(timeUtil.addLimit(LocalDateTime.now()));
            sessionService.saveSession(session);
        } else {
            bot.executeMsg(
                    msgCreatorService.createNotify(
                            session.getChatId(),
                            new RequestWasStoppedException(),
                            session.getLangId())
            );
            commandService.handle(fakerUtil.fakeStop(session), true);
        }
    }

    @Override
    public void sendNextPhotos(Long userId) {
        Session session = sessionService.getSessionIfExist(userId);

        if (session != null) {
            session.setLock(false);
            List<AgencyOffer> offers = offerRepository.getAllBySessionId(session.getId());
            for (int i = 0; i < offers.size(); i++) {
                if (i >= Integer.parseInt(MAX_OFFERS_COUNT)) {
                    session.setNextMessageId(null);
                    sendNextButton(session);
                    return;
                }
                sendNextPhoto(session, offers.get(i));
            }
            session.setNextMessageId(null);
            sessionService.saveSession(session);
        }
    }


    private void sendActions(AgencyOffer offer, Session session, InputFile inputFile) {
        int offersCount = session.getOffersCount();
        if (!session.getLock()) {
            sendMessageNSaveOffer(offer, session.getChatId(), inputFile);
            if (offersCount % Integer.parseInt(MAX_OFFERS_COUNT) == 0) {
                session.setLock(true);
            }
            session.setSentCount(session.getSentCount() + 1);
        } else {
            sendNextButton(session);
            offer.setFilePath(fileUtil.savePhoto(offer.getFile()));
            offerRepository.save(offer);
        }
        sessionService.saveSession(session);
    }


    private void sendNextPhoto(Session session, AgencyOffer offer) {
        InputFile inputFile = fileUtil.pathToInputFile(offer.getFilePath());
        sendActions(offer, session, inputFile);
        fileUtil.deleteWithPath(offer.getFilePath());
        offer.setFilePath(null);
        offerRepository.save(offer);
    }


    private void sendMessageNSaveOffer(AgencyOffer offer, Long chatId, InputFile inputFile) {
        Message message = this.bot.executePhoto(msgCreatorService.createPhoto(chatId, inputFile));
        offer.setMessageId(message.getMessageId());
        offerRepository.save(offer);
    }

    private void sendNextButton(Session session) {
        Question nextQuestion = questionRepository.getByState(StaticStates.NEXT.toString());
        if (session.getNextMessageId() == null) {
            Message message = bot.executeMsg(msgCreatorService.createNextBtn(session, nextQuestion));
            session.setNextMessageId(message.getMessageId());
            sessionService.saveSession(session);
        } else {
            bot.executeMsg(msgCreatorService.updateNextBtn(session, nextQuestion));
        }
    }


}
