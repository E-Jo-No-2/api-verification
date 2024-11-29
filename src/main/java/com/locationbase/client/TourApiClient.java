package com.locationbase.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TourApiClient {

    private static final String TOUR_API_KEY = "VTV36ioiwVsBQXHNheZAGl7tUbPcXXJmkzE2t9yAP5K%2BmEUhqRKNe%2BX4EGswNt7mHbEQ9wd8N9zX2%2FiFQi95gg%3D%3D";
    private static final String BASE_URL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService";

    public JSONObject fetchNearbyTourSpots(String latitude, String longitude, double radius) {
        try {
            String encodedApiKey = URLEncoder.encode(TOUR_API_KEY, StandardCharsets.UTF_8);
            String queryParams = String.format(
                    "&mapX=%s&mapY=%s&radius=%.1f&listYN=Y&arrange=E&numOfRows=50&pageNo=1&_type=json",
                    longitude, latitude, radius
            );

            String url = String.format("%s/locationBasedList?ServiceKey=%s%s", BASE_URL, encodedApiKey, queryParams);

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            return new JSONObject(response).getJSONObject("response");
        } catch (Exception e) {
            System.err.println("Error calling Tour API: " + e.getMessage());
            return null;
        }
    }
}
