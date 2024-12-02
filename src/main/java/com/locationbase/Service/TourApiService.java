package com.locationbase.Service;

import com.locationbase.DTO.LandMarkDTO;
import com.locationbase.Client.TourApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TourApiService {

    // 로깅을 위한 Logger 설정
    private static final Logger logger = LoggerFactory.getLogger(TourApiService.class);

    private final TourApiClient tourApiClient;

    public TourApiService(TourApiClient tourApiClient) {
        this.tourApiClient = tourApiClient;
    }

    /**
     * 사용자 주변 관광지를 테마별로 그룹화하여 반환
     * @param latitude 위도
     * @param longitude 경도
     * @return 테마별로 그룹화된 관광지 목록
     */
    public Map<String, List<LandMarkDTO>> getNearbySpotsByTheme(String longitude, String latitude) {
        logger.info("주변 관광지 조회 시작:  경도={},위도={}", longitude, latitude);

        JSONObject response = tourApiClient.NearbyTourSpots(longitude, latitude);

        if (response == null) {
            logger.error("Tour API 데이터 요청 실패");
            throw new RuntimeException("Tour API로부터 데이터를 가져오지 못했습니다.");
        }
        // JSON 구조 디버깅 출력
        System.out.println("API 응답 데이터: " + response.toString(2));
        if (!response.has("response")) {
            logger.error("API 응답에 'response' 키가 없습니다: {}", response.toString());
            throw new RuntimeException("Invalid API response structure.");
        }

        // 응답 성공 여부 확인
        if (!response.getJSONObject("response").getJSONObject("header").getString("resultCode").equals("0000")) {
            logger.error("API 응답 실패: {}", response.toString(2));
            throw new RuntimeException("Tour API 호출 실패");
        }

        logger.info("Tour API 응답 수신 성공");
        List<LandMarkDTO> spots = parseSpots(response);
        logger.info("응답 데이터에서 {}개의 관광지를 파싱 완료", spots.size());

        Map<String, List<LandMarkDTO>> groupedSpots = groupSpotsByTheme(spots);
        logger.info("관광지를 {}개의 테마로 그룹화 완료", groupedSpots.size());

        return groupedSpots;
    }

    /**
     * API 응답에서 관광지 데이터를 파싱
     * @param response Tour API 응답 JSON 객체
     * @return 파싱된 관광지 목록
     */
    private List<LandMarkDTO> parseSpots(JSONObject response) {
        List<LandMarkDTO> spots = new ArrayList<>();
        try {
            // "response" 키 검증
            if (!response.has("response")) {
                logger.error("JSON 응답에 'response' 키가 없습니다.");
                throw new RuntimeException("Invalid JSON 응답 구조: 'response' 키가 없습니다.");
            }

            JSONObject responseObj = response.getJSONObject("response");

            // "body" 키 검증
            if (!responseObj.has("body")) {
                logger.error("JSON 응답에 'body' 키가 없습니다.");
                throw new RuntimeException("Invalid JSON 응답 구조: 'body' 키가 없습니다.");
            }

            JSONObject body = responseObj.getJSONObject("body");

            // "items.item" 검증
            JSONObject itemsObj = body.optJSONObject("items");
            if (itemsObj == null || !itemsObj.has("item")) {
                logger.error("JSON 응답에 'items.item' 키가 없습니다.");
                throw new RuntimeException("Invalid JSON 응답 구조: 'items.item' 키가 없습니다.");
            }

            JSONArray items = itemsObj.getJSONArray("item");

            // 아이템 파싱
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                System.out.println("파싱된 아이템: " + item.toString(2));

                // 관광지 데이터 파싱
                LandMarkDTO spot = new LandMarkDTO();
                spot.setLandmark_name(item.optString("title"));
                spot.setLongitude(item.optString("mapx"));
                spot.setLatitude(item.optString("mapy"));
                spot.setCat1(item.optString("cat1"));
                spot.setDistance(item.optString("dist"));
                //이 로직은 클라이언트 ( 사용자, 여기서는 ThemaSelects.html)가 이 정보가 필요해요! 라고 요청하는 파라미터를 저장하는 역할입니다.
                //여기서 만약 우리가 실수로 오타를 해서                 spot.setLongitude(item.optString("mapX"));
                // mapy, mapx 이렇게 되있으면 -> 순서대로 들어가  X-> y가 들어가고 Y->x가 들어가요. 이래서 오류가 없어 실행은 돼
                // 그래서 uri를 생성하면 Y -> 127 (경도) 가들어가고 X에 위도가 들어가버려요.
                System.out.println("Parsed DTO: " + spot);
                spots.add(spot);
            }
        } catch (Exception e) {
            logger.error("Tour API 응답 파싱 중 오류 발생: {}", e.getMessage());
        }

        return spots;
    }


    /**
     * 관광지를 테마별로 그룹화
     * @param spots 파싱된 관광지 목록
     * @return 테마별 그룹화된 관광지 맵
     */
    private Map<String, List<LandMarkDTO>> groupSpotsByTheme(List<LandMarkDTO> spots) {
        Map<String, List<LandMarkDTO>> groupedSpots = new HashMap<>();

        for (LandMarkDTO spot : spots) {
            String theme = determineTheme(spot.getCat1());
            groupedSpots.computeIfAbsent(theme, k -> new ArrayList<>()).add(spot);
        }

        logger.info("테마별로 관광지를 그룹화 완료");
        return groupedSpots;
    }

    /**
     * 관광지의 대분류(cat1)를 기반으로 테마 결정
     * @param cat1 대분류 코드
     * @return 테마 이름
     */
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

    /**
     * 오류 응답을 처리하기 위한 헬퍼 메서드
     * @param errorMessage 오류 메시지
     * @return 클라이언트가 처리 가능한 오류 데이터
     */
    public Map<String, Object> handleErrorResponse(String errorMessage) {
        logger.error("오류 처리 중: {}", errorMessage);
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("theme", "error");
        errorResponse.put("message", errorMessage);
        return errorResponse;
    }
}