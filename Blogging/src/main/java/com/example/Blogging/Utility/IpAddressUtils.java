package com.example.Blogging.Utility;

import com.example.Blogging.Models.UserIpAddress;
import com.example.Blogging.repo.UserIpAddressRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class IpAddressUtils {

    private final UserIpAddressRepo userIpAddressRepo;

    private final int LOCK_TIME_DURATION = 10;

    @Autowired
    public IpAddressUtils(UserIpAddressRepo userIpAddressRepo) {
        this.userIpAddressRepo = userIpAddressRepo;
    }

    public String getIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            return ip;
        }
        return null; // Return null or handle no-request-context scenarios
    }

    public boolean doesIpExist(){
        String ipAddressUser = getIpAddress();
        if(ipAddressUser == null) return false;
        UserIpAddress userIpAddress = userIpAddressRepo.findByIpAddress(ipAddressUser);
        return userIpAddress != null;
    }

    public void registerIpAddressOfAUser(){
        String ipAddressUser = getIpAddress();
        if(ipAddressUser != null) {
            UserIpAddress userIpAddress = new UserIpAddress();
            userIpAddress.setIpAddress(ipAddressUser);
            userIpAddress.setFailedLoginAttempt(0);
            userIpAddressRepo.save(userIpAddress);
        }
    }

    public void blockIpAddress(){
        String ipAddressUser = getIpAddress();
        if(ipAddressUser != null) {
            UserIpAddress userIpAddress = userIpAddressRepo.findByIpAddress(ipAddressUser);

            if (userIpAddress.getFailedLoginAttempt() > 9) {
                userIpAddress.setAccountLocked(true);
                userIpAddress.setAccountLockedTimeStamp(LocalDateTime.now());
            } else {
                userIpAddress.setFailedLoginAttempt(userIpAddress.getFailedLoginAttempt() + 1);
            }
            userIpAddressRepo.save(userIpAddress);
        }
    }

    public boolean isAccountBLocked(){
        String ipAddressUser = getIpAddress();
        if (ipAddressUser == null) return false;
        UserIpAddress userIpAddress = userIpAddressRepo.findByIpAddressAndAccountLockedTrue(ipAddressUser);
        return userIpAddress != null;
    }

    public void unlockBlockedIp(){
        List<UserIpAddress> userIpAddresses = userIpAddressRepo.findAllByAccountLockedTrue();

        userIpAddresses.forEach(
                userIpAddress -> {
                    if(userIpAddress.getAccountLockedTimeStamp().plusMinutes(LOCK_TIME_DURATION).isBefore(LocalDateTime.now())){
                        userIpAddress.setFailedLoginAttempt(0);
                        userIpAddress.setAccountLocked(false);
                        userIpAddress.setAccountLockedTimeStamp(null);
                        userIpAddressRepo.save(userIpAddress);
                    }
                }
        );

    }

}
