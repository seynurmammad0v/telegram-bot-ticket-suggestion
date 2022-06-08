package az.telegram.bot.controller;

import az.telegram.bot.facade.TelegramFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@RestController
public class WebHookController {
    private final TelegramFacade facade;

    public WebHookController(TelegramFacade facade) {
        this.facade = facade;
    }

    @PostMapping("callback/webhook")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) throws TelegramApiException, IOException {
        return facade.handleUpdate(update);
    }

    //for testing ngrok
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("health");
    }
}
