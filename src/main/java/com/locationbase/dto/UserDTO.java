package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserDTO {
    private String userId;        // 사용자의 고유 ID (Primary Key)
    private String password;      // 사용자의 비밀번호
    private String name;          // 사용자의 이름
    private String email;         // 사용자의 이메일 (unique)
    private Date birthDate;       // 사용자의 생년월일
    private String phoneNumber;   // 사용자의 전화번호
}
