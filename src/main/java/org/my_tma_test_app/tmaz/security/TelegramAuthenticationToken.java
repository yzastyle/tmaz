package org.my_tma_test_app.tmaz.security;

import org.my_tma_test_app.tmaz.model.TelegramUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class TelegramAuthenticationToken extends AbstractAuthenticationToken {

    private final TelegramUser principal;
    private final Jwt jwt;

    public TelegramAuthenticationToken(Jwt jwt, Collection<GrantedAuthority> auths, TelegramUser user) {
        super(auths);
        this.jwt = jwt;
        this.principal = user;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return jwt.getTokenValue();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
