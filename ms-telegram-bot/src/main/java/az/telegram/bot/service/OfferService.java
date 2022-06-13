package az.telegram.bot.service;

import az.telegram.bot.model.OfferDTO;
import org.springframework.http.HttpStatus;

public interface OfferService {
    HttpStatus sendOffer(Long sessionId, OfferDTO offer) ;
}