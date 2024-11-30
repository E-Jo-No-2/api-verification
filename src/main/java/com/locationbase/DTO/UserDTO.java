package  com.locationbase.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private String userId;
    private String name;
    private String email;
    private Date birth_date;
    private String phone_number;
}
