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
}
