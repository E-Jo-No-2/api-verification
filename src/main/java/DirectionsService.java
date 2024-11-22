import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirectionsService {

    private final String CLIENT_ID = "YOUR_CLIENT_ID"; // 네이버 API Client ID
    private final String CLIENT_SECRET = "YOUR_CLIENT_SECRET"; // 네이버 API Client Secret

    public List<double[]> getOptimalPath(String start, String goal, List<String> waypoints) {
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving";

        try {
            // 파라미터 URL 인코딩
            String encodedStart = URLEncoder.encode(start, StandardCharsets.UTF_8);
            String encodedGoal = URLEncoder.encode(goal, StandardCharsets.UTF_8);
            String encodedWaypoints = waypoints.isEmpty() ? "" :
                    URLEncoder.encode(String.join("|", waypoints), StandardCharsets.UTF_8);

            // 요청 URL 생성
            String requestUrl = String.format("%s?start=%s&goal=%s%s",
                    apiUrl, encodedStart, encodedGoal,
                    waypoints.isEmpty() ? "" : "&waypoints=" + encodedWaypoints);

            RestTemplate restTemplate = new RestTemplate();
            List<double[]> path = new ArrayList<>();

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

            // 요청 엔터티 생성
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

            // JSON 응답 파싱
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray pathArray = jsonResponse
                    .getJSONObject("route")
                    .getJSONArray("traoptimal")
                    .getJSONObject(0)
                    .getJSONArray("path");

            // 경로 데이터를 리스트로 변환
            for (int i = 0; i < pathArray.length(); i++) {
                JSONArray coord = pathArray.getJSONArray(i);
                double longitude = coord.getDouble(0);
                double latitude = coord.getDouble(1);
                path.add(new double[]{latitude, longitude});
            }

            return path;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // 오류 시 빈 리스트 반환
        }
    }
}
