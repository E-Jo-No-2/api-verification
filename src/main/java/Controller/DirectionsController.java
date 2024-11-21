package Controller;

import Service.DirectionsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class DirectionsController {

    private final DirectionsService directionsService;

    public DirectionsController(DirectionsService directionsService) {
        this.directionsService = directionsService;
    }

    @GetMapping("/map")
    public String showMap(@RequestParam String start,
                          @RequestParam String goal,
                          @RequestParam(required = false) String waypoints,
                          Model model) {
        List<String> waypointList = (waypoints != null && !waypoints.isEmpty())
                ? Arrays.asList(waypoints.split("\\|"))
                : List.of();

        List<double[]> path = directionsService.getOptimalPath(start, goal, waypointList);
        model.addAttribute("path", path);
        return "map";
    }
    public List<double[]> getOptimalPath(String start, String goal, List<String> waypoints) {
        String apiUrl = "https://naveropenapi/map/directions/v5?start=" + start +
                "&goal=" + goal +
                (waypoints.isEmpty() ? "" : "&waypoints=" + String.join("|", waypoints));

        System.out.println("API URL: " + apiUrl);

        // 실제 API 호출 로직 추가 (예: RestTemplate)
        // 네이버 API 응답 데이터를 경로로 변환하여 반환
        return List.of(); // 테스트 시 빈 데이터 반환
    }

}
