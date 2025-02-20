package com.example.Blogging.Models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_ip_address",
        indexes = @Index(name = "idx_ip_address" , columnList = "ip_address")
)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserIpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",unique = true,nullable = false,updatable = false)
    private UUID id;

    @Column(name = "ip_address" , unique = true,nullable = false,length = 45)
    private String ipAddress;

    @Column(name = "account_locked_time_stamp")
    private LocalDateTime accountLockedTimeStamp;

    @Column(name = "number_of_failed_login_attempt" , nullable = false)
    private int failedLoginAttempt = 0;

    @Column(name = "account_locked" , nullable = false)
    private boolean accountLocked = false;

}
