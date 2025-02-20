package com.example.Blogging.OtpLoginSuccessEvent;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class LoginSuccessEvent extends ApplicationEvent {

    private final UUID userId;
    private final String ipAddress;

    public LoginSuccessEvent(Object source, UUID userId, String ipAddress) {
        super(source);
        this.userId = userId;
        this.ipAddress = ipAddress;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
