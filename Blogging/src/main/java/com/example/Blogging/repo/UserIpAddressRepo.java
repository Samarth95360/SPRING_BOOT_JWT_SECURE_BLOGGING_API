package com.example.Blogging.repo;

import com.example.Blogging.Models.UserIpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserIpAddressRepo extends JpaRepository<UserIpAddress, UUID> {

    UserIpAddress findByIpAddress(String ipAddress);

    List<UserIpAddress> findAllByAccountLockedTrue();

    UserIpAddress findByIpAddressAndAccountLockedTrue(String ipAddress);


}
