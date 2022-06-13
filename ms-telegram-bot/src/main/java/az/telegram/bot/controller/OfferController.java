package az.telegram.bot.controller;

import az.telegram.bot.model.OfferDTO;
import az.telegram.bot.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/{sessionId}")
    public ResponseEntity<Void> sendOffer(@PathVariable Long sessionId,
                                          @Valid @RequestBody OfferDTO offer) {
        return new ResponseEntity<>(offerService.sendOffer(sessionId, offer));
    }

}