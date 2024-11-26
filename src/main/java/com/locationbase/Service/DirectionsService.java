package com.locationbase.Service;

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
import java.util.*;

@Service
public class DirectionsService {

    private final String CLIENT_ID = "zf3b8gnmu2"; // 네이버 API Client ID
    private final String CLIENT_SECRET = "AWc84Y7RPJwSaiIZZz3vmPd3J2FqecHFOtwOSYxi"; // 네이버 API Client Secret

    public Map<String, Object> getRouteWithAllOptions(String start, String goal, List<String> waypoints) {
        Map<String, Object> routeOptions = new HashMap<>();
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving";

        String[] options = {"trafast", "tracomfort", "traoptimal", "traavoidtoll", "traavoidcaronly"};
        for (String option : options) {
            try {
                String encodedStart = URLEncoder.encode(start, StandardCharsets.UTF_8);
                String encodedGoal = URLEncoder.encode(goal, StandardCharsets.UTF_8);
                String encodedWaypoints = waypoints.isEmpty() ? "" :
                        URLEncoder.encode(String.join("|", waypoints), StandardCharsets.UTF_8);

                String requestUrl = String.format("%s?start=%s&goal=%s%s&option=%s",
                        apiUrl, encodedStart, encodedGoal,
                        waypoints.isEmpty() ? "" : "&waypoints=" + encodedWaypoints, option);

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
                headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONObject routeObject = jsonResponse.getJSONObject("route");
                if (routeObject.has(option)) {
                    routeOptions.put(option, routeObject.getJSONArray(option).getJSONObject(0).toMap());
                } else {
                    routeOptions.put(option, "No data available for this option");
                }

            } catch (Exception e) {
                routeOptions.put(option, "Error fetching data for option: " + e.getMessage());
            }
        }

        return routeOptions;
    }

    public Map<String, Object> getTrafficCongestionData(Map<String, Object> routeOptions) {
        Map<String, Object> trafficData = new HashMap<>();

        for (String option : routeOptions.keySet()) {
            Object route = routeOptions.get(option);

            if (route instanceof Map) {
                Map<?, ?> routeMap = (Map<?, ?>) route;
                if (routeMap.containsKey("section")) {
                    List<Map<String, Object>> sections = (List<Map<String, Object>>) routeMap.get("section");

                    List<Map<String, Object>> congestionInfo = new ArrayList<>();
                    for (Map<String, Object> section : sections) {
                        Map<String, Object> congestion = new HashMap<>();
                        congestion.put("name", section.getOrDefault("name", "Unnamed Road"));
                        congestion.put("distance", section.get("distance"));
                        congestion.put("speed", section.get("speed"));
                        congestion.put("congestion", section.get("congestion"));
                        congestionInfo.add(congestion);
                    }
                    trafficData.put(option, congestionInfo);
                }
            }
        }

        return trafficData;
    }

    public Map<String, Object> getTrafficInfoWithRoadNameAndCongestion(List<String> routePoints) {
        Map<String, Object> trafficInfo = new HashMap<>();
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving";

        try {
            String encodedPoints = URLEncoder.encode(String.join("|", routePoints), StandardCharsets.UTF_8);
            String requestUrl = String.format("%s?start=%s", apiUrl, encodedPoints);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray sections = jsonResponse.getJSONObject("route")
                    .getJSONArray("traoptimal")
                    .getJSONObject(0)
                    .getJSONArray("section");

            List<Map<String, Object>> congestionData = new ArrayList<>();
            for (int i = 0; i < sections.length(); i++) {
                JSONObject section = sections.getJSONObject(i);

                Map<String, Object> sectionData = new HashMap<>();
                sectionData.put("name", section.optString("name", "Unnamed Road"));
                sectionData.put("congestion", section.getInt("congestion"));
                sectionData.put("speed", section.getInt("speed"));
                sectionData.put("distance", section.getInt("distance"));

                congestionData.add(sectionData);
            }

            trafficInfo.put("congestionData", congestionData);

        } catch (Exception e) {
            trafficInfo.put("error", "Error fetching traffic information: " + e.getMessage());
        }

        return trafficInfo;
    }
}
