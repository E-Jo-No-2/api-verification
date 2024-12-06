package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDTO {
    private String user_id;
    private String password;
    private String name;
    private String email;
    private Date birth_date;
    private String phone_number;
}
