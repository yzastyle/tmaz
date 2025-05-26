package org.my_tma_test_app.tmaz.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.my_tma_test_app.tmaz.model.TelegramUser;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramAuthData {

    private TelegramUser user;
    private Instant authDate;
    private String queryId;
    private String hash;
    private String rawData;
}

