package org.my_tma_test_app.tmaz.config;

import org.my_tma_test_app.tmaz.security.service.impl.TelegramDataValidatorServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

    @Bean
    TelegramDataValidatorServiceImpl telegramBotFromProperties(TelegramBotSourceProperties telegramBotSourceProperties) {
        return new TelegramDataValidatorServiceImpl(telegramBotSourceProperties);
    }

    @Bean
    @ConfigurationProperties("telegram.bot")
    TelegramBotSourceProperties telegramBotSourceProperties() {
        return new TelegramBotSourceProperties();
    }
}
