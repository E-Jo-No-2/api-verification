package com.locationbase.client;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class TourApiClient {

    private static final String BASE_URL = "https://apis.data.go.kr/B551011/KorService1/locationBasedList1";
    private static final String TOUR_API_KEY = "1ik805mS4HuYvSTPwW755cequ4ZyXbHNaeWO27BLFA6qvgF43027M07miclVbWa%2BK%2FyMVqe1jW2w7IVlpkNRow%3D%3D";

    public JSONObject NearbyTourSpots(String longitude, String latitude) {
        try {
            // URI 생성 (자동 인코딩 비활성화)
            URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                    .queryParam("serviceKey", TOUR_API_KEY) // 이미 인코딩된 키
                    .queryParam("mapX", longitude)
                    .queryParam("mapY", latitude)
                    .queryParam("radius", 3500)
                    .queryParam("numOfRows", "40")
                    .queryParam("MobileOS", "ETC")
                    .queryParam("MobileApp", "AppTest")
                    .queryParam("_type", "json")
                    .build(true) // 자동 인코딩 비활성화
                    .toUri();

            // URI 디버깅
            System.out.println("생성된 URI: " + uri);

            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            System.out.println("요청 헤더: " + headers);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // RestTemplate API 호출
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

            // 응답 처리
            String response = responseEntity.getBody();
            if (response == null || response.isEmpty()) {
                throw new RuntimeException("빈 응답을 받았습니다.");
            }

            // 응답 디버깅
            System.out.println("API 응답: " + response);
            return new JSONObject(response);

        } catch (Exception e) {
            System.err.println("API 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
