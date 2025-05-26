package org.my_tma_test_app.tmaz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.my_tma_test_app.tmaz.security.AuthEntryPoint;
import org.my_tma_test_app.tmaz.security.CustomJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    ApplicationContext context;

    private static final String[] PUBLIC_PATHS = {
            "/",
            "/telegram/auth",
            "/error",
            "/static/**",
            "/css/**",
            "/js/**",
            "/jwt/*"
    };
    @Value("${spring.security.enabled}")
    private boolean securityEnabled;
    @Value("${jwt.secret}")
    private String secret;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        if (Boolean.FALSE.equals(securityEnabled)) {
            return http
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                    .build();
        }

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequest ->
                        httpRequest.requestMatchers(PUBLIC_PATHS).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionConfigurer ->
                        sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                        jwt -> jwt.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAbstractAuthenticationTokenConverter()))
                        .bearerTokenResolver(bearerTokenResolver())
                        .authenticationEntryPoint(new AuthEntryPoint(objectMapper)))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKey key = getSecretKey();


        return NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();


//        SecretKey key = getSecretKey();
//        NimbusJwtDecoder delegate = NimbusJwtDecoder.withSecretKey(key).build();
//        return token -> {
//            try {
//                Jwt jwt = delegate.decode(token);
//                return jwt;
//            } catch (JwtException ex) {
//                throw ex;
//            }
//        };
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey key = getSecretKey();
        String keyId = UUID.randomUUID().toString();

        OctetSequenceKey jwk = new OctetSequenceKey.Builder(key.getEncoded())
                .keyID(keyId)
                .algorithm(JWSAlgorithm.HS256)
                .keyUse(KeyUse.SIGNATURE)
                .build();

        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        NimbusJwtEncoder encode = new NimbusJwtEncoder(jwkSource);

        return parameters -> {
            JwsHeader header = JwsHeader.with(MacAlgorithm.HS256)
                    .keyId(keyId)
                    .build();
            return encode.encode(JwtEncoderParameters.from(header, parameters.getClaims()));
        };
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAbstractAuthenticationTokenConverter() {
        return new CustomJwtAuthenticationConverter();
    }

    @Bean
    public BearerTokenResolver bearerTokenResolver() {
        return request -> {

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("tg-auth".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        };
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
