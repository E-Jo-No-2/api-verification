package com.locationbase.Service;

import com.locationbase.DTO.PlannerDTO;
import com.locationbase.Domain.Repository.*;
import com.locationbase.Entity.LandMarkEntity;
import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Entity.UserEntity;
import com.locationbase.Entity.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlannerService {

    @Autowired
    private PlannerRepository plannerRepository;

    @Autowired
    private LandMarkRepository landMarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    /**
     * Create a new planner from the provided PlannerDTO.
     */
    public PlannerEntity createPlanner(PlannerDTO plannerDTO) {
        // Find the user, weather, and theme by their IDs/names from the DTO
        UserEntity user = userRepository.findById(Integer.valueOf(plannerDTO.getUser_id()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        WeatherEntity weather = weatherRepository.findById(plannerDTO.getWeather_date())
                .orElseThrow(() -> new RuntimeException("Weather not found"));

        // Create a new PlannerEntity and set the values
        PlannerEntity planner = new PlannerEntity();
        planner.setUser_id(user);
        planner.setWeather_date(weather);
        planner.setStart_date(plannerDTO.getStart_date());
        planner.setTheme_name(plannerDTO.getTheme_name());

        // Save and return the planner
        return plannerRepository.save(planner);
    }

    /**
     * Update an existing planner by its ID using the provided PlannerDTO.
     */
    public PlannerEntity updatePlanner(int plannerId, PlannerDTO plannerDTO) {
        // Find the existing planner by ID
        PlannerEntity existingPlanner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found"));

        // Find and set the user, weather, and theme from the DTO
        UserEntity user = userRepository.findById(plannerDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WeatherEntity weather = weatherRepository.findById(plannerDTO.getWeather_date())
                .orElseThrow(() -> new RuntimeException("Weather not found"));

        // Update the existing planner with new values
        existingPlanner.setUser_id(user);
        existingPlanner.setWeather_date(weather);
        existingPlanner.setStart_date(plannerDTO.getStart_date());
        existingPlanner.setTheme_name(plannerDTO.getTheme_name());

        // Save and return the updated planner
        return plannerRepository.save(existingPlanner);
    }

    /**
     * Delete an existing planner by its ID.
     */
    public void deletePlanner(int planner_id) {
        PlannerEntity planner = plannerRepository.findById(planner_id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));
        plannerRepository.delete(planner);
    }

    /**
     * Get all planners for a specific user.
     */
    public List<PlannerEntity> getPlannersByUser(String user_id) {
        return plannerRepository.findByUserId(user_id);
    }

    /**
     * Recommend landmarks based on weather conditions for a specific date.
     */
    public List<LandMarkEntity> recommendByWeather(LocalDate date) {
        // Fetch weather data from an external service (this is mocked for now)
        String weatherCondition = fetchWeatherCondition(date);

        // Depending on the weather, recommend landmarks
        List<LandMarkEntity> recommendedLandmarks;

        if ("Clear".equalsIgnoreCase(weatherCondition)) {
            recommendedLandmarks = landMarkRepository.findByThemeName("Outdoor");
        } else if ("Rain".equalsIgnoreCase(weatherCondition) || "Clouds".equalsIgnoreCase(weatherCondition)) {
            recommendedLandmarks = landMarkRepository.findByThemeName("Indoor");
        } else {
            recommendedLandmarks = landMarkRepository.findAll(); // Default case: recommend all landmarks
        }

        return recommendedLandmarks;
    }

    /**
     * Simulate fetching weather data for the given date (this can be replaced with an actual weather API).
     */
    private String fetchWeatherCondition(LocalDate date) {
        // Example: Hardcoded weather for demonstration purposes
        if (date.isBefore(LocalDate.now())) {
            return "Rain";  // Simulating rainy weather for past dates
        }
        return "Clear";  // Simulating clear weather for future dates
    }
}
