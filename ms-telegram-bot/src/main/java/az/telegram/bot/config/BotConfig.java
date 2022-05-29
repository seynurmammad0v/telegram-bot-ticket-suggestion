package az.telegram.bot.config;

import az.telegram.bot.command.StartCommand;
import az.telegram.bot.command.StopCommand;
import az.telegram.bot.model.Bot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    private String botPath;

    @Bean
    public Bot telegramBot() throws TelegramApiException {
        Bot telegramBot = Bot.builder()
                .botPath(botPath)
                .botUsername(botUserName)
                .webHookPath(webHookPath)
                .botToken(botToken)
                .build();
        telegramBot.execute(setCommands());
        telegramBot.setWebhook(new SetWebhook(webHookPath));
        return telegramBot;
    }

    public SetMyCommands setCommands() {
        return SetMyCommands.builder()
                .commands(List.of(new StartCommand(), new StopCommand()))
                .build();
    }

}
