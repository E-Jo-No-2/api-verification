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
        // 경유지 처리
        List<String> waypointList = (waypoints != null && !waypoints.isEmpty())
                ? Arrays.asList(waypoints.split("\\|"))
                : List.of();

        // 서비스 호출
        List<double[]> path = directionsService.getOptimalPath(start, goal, waypointList);

        // 경로 데이터를 모델에 추가
        model.addAttribute("path", path);
        return "map"; // map.html 반환
    }
}
