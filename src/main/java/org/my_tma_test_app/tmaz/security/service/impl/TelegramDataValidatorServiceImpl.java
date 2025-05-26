package org.my_tma_test_app.tmaz.security.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import org.my_tma_test_app.tmaz.config.TelegramBotSourceProperties;
import org.my_tma_test_app.tmaz.security.exception.ValidationException;
import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.my_tma_test_app.tmaz.security.service.TelegramDataValidatorService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Data
public class TelegramDataValidatorServiceImpl implements TelegramDataValidatorService {

    private final String username;
    private final String token;
    private final boolean authDateCheckEnabled;
    private final long authDateMaxAgeSeconds;
    private final String telegramDataCheckKey;
    private final String hmacSha256;
    private final boolean enabled;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public TelegramDataValidatorServiceImpl(TelegramBotSourceProperties telegramBotSourceProperties) {
        this.username = telegramBotSourceProperties.getUsername();
        this.token = telegramBotSourceProperties.getToken();
        this.authDateCheckEnabled = telegramBotSourceProperties.isAuthDateCheckEnabled();
        this.authDateMaxAgeSeconds = telegramBotSourceProperties.getAuthDateMaxAgeSeconds();
        this.telegramDataCheckKey = telegramBotSourceProperties.getTelegramDataCheckKey();
        this.hmacSha256 = telegramBotSourceProperties.getHmacSha256();
        this.enabled = telegramBotSourceProperties.isEnabled();
    }

    @SneakyThrows
    @Override
    public Optional<TelegramUser> validate(String initData) {
        Map<String, String> data = parseInitData(initData);

        validateInputParams(data);

        String dataCheckString = makeDataCheckString(data);

        String calcHash = calculateHash(dataCheckString);
        String receivedHash = data.get("hash");

        if (enabled && !calcHash.equalsIgnoreCase(receivedHash)) {
            System.out.printf("Hash validation failed. Received: %s, Calculated: %s%n", receivedHash, calcHash);
            throw new ValidationException("Invalid hash");
        }

        return Optional.of(parseUserData(data.get("user")));
    }

    private Map<String, String> parseInitData(String initData) {
        Map<String, String> data;
        String[] kvArray = initData.split("&");
        data = Arrays.stream(kvArray)
                .map(kv -> kv.split("="))
                .collect(Collectors.toMap(p -> URLDecoder.decode(p[0], StandardCharsets.UTF_8),
                        p -> URLDecoder.decode(p[1], StandardCharsets.UTF_8)));
        return data;
    }

    @SneakyThrows
    private TelegramUser parseUserData(String userInfo) {
        JsonNode jsonNode = objectMapper.readTree(userInfo);

        return TelegramUser.builder()
                .id(jsonNode.get("id").asLong())
                .firstName(jsonNode.get("first_name").asText())
                .lastName(jsonNode.get("last_name").asText())
                .photoUrl(jsonNode.get("photo_url").asText())
                .username(jsonNode.get("username").asText())
                .languageCode(jsonNode.get("language_code").asText())
                .build();
    }

    @SneakyThrows
    private void validateAuthDate(long authDate) {
        if (!authDateCheckEnabled) {
            System.out.println("debug: auth date check is disabled");
            return;
        }

        long currentSeconds = Instant.now().getEpochSecond();
        long diff = currentSeconds - authDate;

        if (diff > authDateMaxAgeSeconds) {
            throw new ValidationException(
                    String.format("Auth date is too old: %d seconds ago (max allowed: %d)",
                            diff, authDateMaxAgeSeconds)
            );
        }
        if (diff < 0) {
            throw new ValidationException("Auth date is in the future");
        }
    }

    private String makeDataCheckString(Map<String, String> data) {
        String dataCheckString = data.entrySet().stream()
                .filter(x -> !x.getKey().equalsIgnoreCase("hash"))
                .sorted(Map.Entry.comparingByKey())
                .map(x -> x.getKey() +"="+ x.getValue())
                .collect(Collectors.joining("\n"));
        return dataCheckString;
    }

    private String calculateHash(String dataCheckString) {
        byte[] secretKey = hmacSha256(telegramDataCheckKey.getBytes(StandardCharsets.UTF_8),
                token.getBytes(StandardCharsets.UTF_8));
        byte[] calcHash = hmacSha256(secretKey, dataCheckString.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(calcHash);
    }

    @SneakyThrows
    private byte[] hmacSha256(byte[] key, byte[] data) {
        Mac mac = Mac.getInstance(hmacSha256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, hmacSha256);
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }

    private void validateInputParams(Map<String, String> data) throws ValidationException {
        String receivedHash = data.get("hash");
        if (receivedHash == null || receivedHash.isEmpty()) {
            throw new ValidationException("Hash parameter is missing");
        }
        String authDate = data.get("auth_date");
        if (authDate == null || authDate.isEmpty()) {
            throw new ValidationException("auth_date parameter is missing");
        }

        validateAuthDate(Long.parseLong(authDate));
    }
}
