package com.example.Blogging.service.Security;

import com.example.Blogging.Models.User;
import com.example.Blogging.Utility.IpAddressUtils;
import com.example.Blogging.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final IpAddressUtils ipAddressUtils;
    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(IpAddressUtils ipAddressUtils, UserRepo userRepo){
        this.ipAddressUtils = ipAddressUtils;
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);

        if(user == null){
            ipAddressUtils.blockIpAddress();
            throw new UsernameNotFoundException("User with this mail doesn't exist");
        }
        return new UserService(user);

    }
}
