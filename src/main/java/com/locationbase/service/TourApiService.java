package com.locationbase.service;

import com.locationbase.dto.LandmarkDTO;
import com.locationbase.client.TourApiClient;
import com.locationbase.domain.repository.PlannerSpotRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TourApiService {

    private static final Logger logger = LoggerFactory.getLogger(TourApiService.class);
    private final TourApiClient tourApiClient;
    private final PlannerSpotRepository plannerSpotRepository;

    public TourApiService(TourApiClient tourApiClient, PlannerSpotRepository plannerSpotRepository) {
        this.tourApiClient = tourApiClient;
        this.plannerSpotRepository = plannerSpotRepository;
    }

    public Map<String, List<LandmarkDTO>> getNearbySpotsByTheme(String longitude, String latitude) {
        logger.info("주변 관광지 조회 시작: 경도={}, 위도={}", longitude, latitude);

        JSONObject response = tourApiClient.NearbyTourSpots(longitude, latitude);

        if (response == null) {
            logger.error("Tour API 데이터 요청 실패");
            throw new RuntimeException("Tour API로부터 데이터를 가져오지 못했습니다.");
        }

        System.out.println("API 응답 데이터: " + response.toString(2));
        if (!response.has("response")) {
            logger.error("API 응답에 'response' 키가 없습니다: {}", response.toString());
            throw new RuntimeException("유효하지 않은 API 응답 구조.");
        }

        if (!response.getJSONObject("response").getJSONObject("header").getString("resultCode").equals("0000")) {
            logger.error("API 응답 실패: {}", response.toString(2));
            throw new RuntimeException("Tour API 호출 실패");
        }

        logger.info("Tour API 응답 수신 성공");
        List<LandmarkDTO> spots = parseSpots(response);
        logger.info("응답 데이터에서 {}개의 관광지를 파싱 완료", spots.size());

        Map<String, List<LandmarkDTO>> groupedSpots = groupSpotsByTheme(spots);
        logger.info("관광지를 {}개의 테마로 그룹화 완료", groupedSpots.size());

        return groupedSpots;
    }

    private List<LandmarkDTO> parseSpots(JSONObject response) {
        List<LandmarkDTO> spots = new ArrayList<>();
        try {
            if (!response.has("response")) {
                logger.error("JSON 응답에 'response' 키가 없습니다.");
                throw new RuntimeException("유효하지 않은 JSON 응답 구조: 'response' 키가 없습니다.");
            }

            JSONObject responseObj = response.getJSONObject("response");

            if (!responseObj.has("body")) {
                logger.error("JSON 응답에 'body' 키가 없습니다.");
                throw new RuntimeException("유효하지 않은 JSON 응답 구조: 'body' 키가 없습니다.");
            }

            JSONObject body = responseObj.getJSONObject("body");

            JSONObject itemsObj = body.optJSONObject("items");
            if (itemsObj == null || !itemsObj.has("item")) {
                logger.error("JSON 응답에 'items.item' 키가 없습니다.");
                throw new RuntimeException("유효하지 않은 JSON 응답 구조: 'items.item' 키가 없습니다.");
            }

            JSONArray items = itemsObj.getJSONArray("item");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                System.out.println("파싱된 아이템: " + item.toString(2));

                LandmarkDTO spot = new LandmarkDTO();
                spot.setLandmarkName(item.optString("title"));
                spot.setLongitude(item.optString("mapx"));
                spot.setLatitude(item.optString("mapy"));
                spot.setCat1(item.optString("cat1"));
                spot.setDistance(item.optString("dist"));

                System.out.println("Parsed DTO: " + spot);
                spots.add(spot);
            }
        } catch (Exception e) {
            logger.error("Tour API 응답 파싱 중 오류 발생: {}", e.getMessage());
        }

        return spots;
    }

    private Map<String, List<LandmarkDTO>> groupSpotsByTheme(List<LandmarkDTO> spots) {
        Map<String, List<LandmarkDTO>> groupedSpots = new HashMap<>();

        for (LandmarkDTO spot : spots) {
            String theme = determineTheme(spot.getCat1());
            groupedSpots.computeIfAbsent(theme, k -> new ArrayList<>()).add(spot);
        }

        logger.info("테마별로 관광지를 그룹화 완료");
        return groupedSpots;
    }

    private String determineTheme(String cat1) {
        logger.debug("테마 결정 중: cat1={} ", cat1);
        switch (cat1) {
            case "A01":
            case "A02":
                return "관광 및 문화";
            case "A03":
                return "레저 및 액티비티";
            case "A04":
                return "쇼핑 및 음식";
            case "A05":
                return "여행 및 숙박";
            default:
                return "기타";
        }
    }

    public Map<String, Object> handleErrorResponse(String errorMessage) {
        logger.error("오류 처리 중: {}", errorMessage);
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("theme", "error");
        errorResponse.put("message", errorMessage);
        return errorResponse;
    }
}
