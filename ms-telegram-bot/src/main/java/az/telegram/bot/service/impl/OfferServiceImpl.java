package az.telegram.bot.service.impl;

import az.telegram.bot.dao.AgencyOffer;
import az.telegram.bot.dao.OfferDetails;
import az.telegram.bot.dao.Session;
import az.telegram.bot.dao.repository.OfferDetailsRepository;
import az.telegram.bot.model.OfferDTO;
import az.telegram.bot.service.ListenerService;
import az.telegram.bot.service.OfferService;
import az.telegram.bot.service.SessionService;
import az.telegram.bot.utils.ConverterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferDetailsRepository offerDetailsRepository;
    private final SessionService sessionService;
    private final ConverterUtil converterUtil;
    private final ListenerService listenerService;
    @Value("${offerTemplate.path}")
    String templatePath;

    public OfferServiceImpl(OfferDetailsRepository offerDetailsRepository,
                            SessionService sessionService,
                            ConverterUtil converterUtil,
                            ListenerService listenerService) {
        this.offerDetailsRepository = offerDetailsRepository;
        this.sessionService = sessionService;
        this.converterUtil = converterUtil;
        this.listenerService = listenerService;

    }

    @Override
    public HttpStatus sendOffer(Long sessionId, OfferDTO offerDTO) {
        Session session = sessionService.getSessionById(sessionId);
        if (session != null) {
            OfferDetails offer = offerDetailsRepository.save(OfferDetails.builder()
                    .price(offerDTO.getPrice())
                    .dateInterim(offerDTO.getDateInterim())
                    .description(offerDTO.getDescription())
                    .notes(offerDTO.getNotes())
                    .build());

            byte[] offerImage = converterUtil.htmlToImage(templatePath,
                    offer,
                    "TEST_COMPANY_NAME");
            listenerService.sendPhoto(AgencyOffer.builder()
                    .sessionId(sessionId)
                    .offerDetailsId(offer.getId())
                    .file(offerImage)
                    .agencyUsername("test")
                    .isAccepted(false)
                    .build());
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }


}