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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "birth_date")
    private Date birth_date;

    @Column(name = "phone_number", length = 15)
    private String phone_number;

    public UserEntity(String password, String name, String email, Date birth_date, String phone_number) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.birth_date = birth_date;
        this.phone_number = phone_number;
    }
}
