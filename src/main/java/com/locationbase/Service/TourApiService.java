package com.locationbase.Service;

import com.locationbase.DTO.LandMarkDTO;
import com.locationbase.client.TourApiClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TourApiService {

    private final TourApiClient tourApiClient;

    public TourApiService(TourApiClient tourApiClient) {
        this.tourApiClient = tourApiClient;
    }

    // 반경 내의 관광지 데이터 가져오기 및 테마별 필터링
    public Map<String, List<LandMarkDTO>> getNearbySpotsByTheme(String latitude, String longitude, double radius) {
        JSONObject response = tourApiClient.fetchNearbyTourSpots(latitude, longitude, radius);
        if (response == null) {
            throw new RuntimeException("Failed to fetch data from Tour API");
        }

        List<LandMarkDTO> spots = parseSpots(response);
        return groupSpotsByTheme(spots); // 테마별로 그룹화하여 반환
    }

    // JSON 응답 데이터를 LandMarkDTO 리스트로 변환
    private List<LandMarkDTO> parseSpots(JSONObject response) {
        List<LandMarkDTO> spots = new ArrayList<>();
        JSONObject body = response.getJSONObject("response").getJSONObject("body");
        JSONArray items = body.optJSONObject("items").optJSONArray("item");

        if (items != null) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                LandMarkDTO spot = new LandMarkDTO();
                spot.setLandmark_name(item.optString("title"));
                spot.setLatitude(item.optString("mapy"));
                spot.setLongitude(item.optString("mapx"));
                spot.setCat1(item.optString("cat1")); // 테마 분류 정보 추가
                spots.add(spot);
            }
        }

        return spots;
    }

    // 테마별로 관광지를 그룹화
    private Map<String, List<LandMarkDTO>> groupSpotsByTheme(List<LandMarkDTO> spots) {
        Map<String, List<LandMarkDTO>> groupedSpots = new HashMap<>();

        for (LandMarkDTO spot : spots) {
            String theme = determineTheme(spot.getCat1());
            groupedSpots.computeIfAbsent(theme, k -> new ArrayList<>()).add(spot);
        }

        return groupedSpots;
    }

    // cat1 값을 기반으로 테마 결정
    private String determineTheme(String cat1) {
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
}
