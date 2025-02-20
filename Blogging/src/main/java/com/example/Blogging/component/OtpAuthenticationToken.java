package com.example.Blogging.component;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.UUID;

public class OtpAuthenticationToken extends AbstractAuthenticationToken {

    private final Object userId;
    private final String otp;

    public OtpAuthenticationToken(Object userId , String otp) {
        super((Collection)null);
        this.userId = userId;
        this.otp = otp;
        this.setAuthenticated(false);
    }

    public OtpAuthenticationToken(Object userId , String otp, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.otp = otp;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return otp;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

}
