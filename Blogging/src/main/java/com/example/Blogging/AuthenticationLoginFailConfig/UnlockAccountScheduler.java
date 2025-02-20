package com.example.Blogging.AuthenticationLoginFailConfig;

import com.example.Blogging.Utility.IpAddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class UnlockAccountScheduler {

    private final UnlockAccountService unlockAccountService;
    private final IpAddressUtils ipAddressUtils;


    @Autowired
    public UnlockAccountScheduler(UnlockAccountService unlockAccountService, IpAddressUtils ipAddressUtils) {
        this.unlockAccountService = unlockAccountService;
        this.ipAddressUtils = ipAddressUtils;
    }

    @Scheduled(fixedRate = 60000)
    public void unlockAccount(){
        unlockAccountService.unlockAccountService();
    }

    @Scheduled(fixedRate = 60000)
    public void unlockIpAccount(){
        ipAddressUtils.unlockBlockedIp();
    }

}
