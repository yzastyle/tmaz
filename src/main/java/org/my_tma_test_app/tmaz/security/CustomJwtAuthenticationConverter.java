package org.my_tma_test_app.tmaz.security;

import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {


        @SuppressWarnings("unchecked")
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null) {
            roles = List.of();
        }

        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());

        String principalName = jwt.getSubject();
        String firstName    = jwt.getClaim("firstName");
        String username     = jwt.getClaim("username");
        String languageCode = jwt.getClaim("languageCode");
        String photoUrl     = jwt.getClaim("photoUrl");

        TelegramUser principal = TelegramUser.builder()
                .id(Long.valueOf(principalName))
                .firstName(firstName)
                .username(username)
                .languageCode(languageCode)
                .photoUrl(photoUrl)
                .build();

        return new TelegramAuthenticationToken(jwt, authorities, principal);
    }

}
