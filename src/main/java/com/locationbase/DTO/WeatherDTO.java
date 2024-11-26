package  com.locationbase.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class WeatherDTO {

    private Date date;
    private String weather;
    private String icon_code;
    private String icon_url;
}
