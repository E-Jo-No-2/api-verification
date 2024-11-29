package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    public UserEntity(String userId, String password, String name, String email, Date birthDate, String phoneNumber) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
    }
}
