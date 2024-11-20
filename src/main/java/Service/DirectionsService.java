package Service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class DirectionsService {
    private final String CLIENT_ID = "zf3b8gnmu2";
    private final String CLIENT_SECRET = "AWc84Y7RPJwSaiIZZz3vmPd3J2FqecHFOtwOSYxi";
    public List<double[]> getOptimalPath(String start, String goal, List<String> waypoints) {
    String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving?start=127.1058342%2C37.359708&goal=129.075986%2C35.179470' ";
    String waypointparam = String.join("|", waypoints);
        String requestUrl = String.format("%s?start=%s&goal=%s&waypoints=%s", apiUrl, start, goal, waypointparam);
        RestTemplate restTemplate = new RestTemplate();
        List<double[]> path = new ArrayList<double[]>();
        try {
            //예시 해더
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("X-NCP-APIG-API-KEY", CLIENT_ID);
            headers.set("X-NCP-APIG-API-KEY", CLIENT_SECRET);

            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            // API 호출
            org.springframework.http.ResponseEntity<String> response =
                    restTemplate.exchange(requestUrl, org.springframework.http.HttpMethod.GET, entity, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray  pathArray = jsonResponse
                    .getJSONObject("route")
                    .getJSONArray("traoptimal")
                    .getJSONObject(0)
                    .getJSONArray("path");
            for (int i = 0; i < pathArray.length(); i++) {
                JSONArray coord = pathArray.getJSONArray(i);
                double longitude = coord.getDouble(0);
                double latitude = coord.getDouble(1);
                path.add(new double[]{latitude, longitude});
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
