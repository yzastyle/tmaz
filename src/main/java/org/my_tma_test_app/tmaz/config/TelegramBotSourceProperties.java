package org.my_tma_test_app.tmaz.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TelegramBotSourceProperties {
    private String token;
    private String username;
    private boolean authDateCheckEnabled;
    private int authDateMaxAgeSeconds;
    private String telegramDataCheckKey;
    private String hmacSha256;
    private boolean enabled;
}
