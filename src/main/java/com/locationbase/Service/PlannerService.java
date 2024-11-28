/*package com.locationbase.Service;

import com.locationbase.DTO.PlannerDTO;
import com.locationbase.Domain.Repository.*;
import com.locationbase.Entity.*;
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
    private LandMarkRepository landmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private WeatherRepository weatherRepository;


    public PlannerEntity createPlanner(PlannerDTO plannerDTO) {
        // Find the user by user ID
        UserEntity user = userRepository.findById(Integer.valueOf(plannerDTO.getUser_id()))
                .orElseThrow(() -> new RuntimeException("User not found"));


        WeatherEntity weather = (WeatherEntity) weatherRepository.findById(plannerDTO.getDate())
                .orElseThrow(() -> new RuntimeException("Weather not found for the specified date"));

        // 랜드마크 찾기
        LandMarkEntity landmark = landmarkRepository.findById(Integer.valueOf(plannerDTO.getLandmark_name()))
                .orElseThrow(() -> new RuntimeException("Landmark not found"));

        // ㅌ
        themeRepository.findById(plannerDTO.getTheme_name())
                .orElseThrow(() -> new RuntimeException("Theme not found"));

        // Create and populate the PlannerEntity
        PlannerEntity planner = new PlannerEntity();
        planner.setUser_id(user);
        planner.setWeather_date(weather);
        planner.setStart_date(plannerDTO.getStart_date());
        planner.setTheme_name(plannerDTO.getTheme_name());
        planner.setLandmark_name(landmark.getLandmark_name());

        // Save and return the new planner
        return plannerRepository.save(planner);
    }


    public PlannerEntity updatePlanner(int planner_id, PlannerDTO plannerDTO) {
        // Find the existing planner by ID
        PlannerEntity existingPlanner = plannerRepository.findById(planner_id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));

        // Find and update user, weather, and landmark information
        UserEntity user = userRepository.findById(Integer.valueOf(plannerDTO.getUser_id()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        WeatherEntity weather = (WeatherEntity) weatherRepository.findById(plannerDTO.getDate())
                .orElseThrow(() -> new RuntimeException("Weather not found for the specified date"));

        LandMarkEntity landmark = landmarkRepository.findById(Integer.valueOf(plannerDTO.getLandmark_name()))
                .orElseThrow(() -> new RuntimeException("Landmark not found"));


        existingPlanner.setUser_id(user);
        existingPlanner.setWeather_date(weather);
        existingPlanner.setStart_date(plannerDTO.getStart_date());
        existingPlanner.setTheme_name(plannerDTO.getTheme_name());
        existingPlanner.setLandmark_name(landmark.getLandmark_name());


        return plannerRepository.save(existingPlanner);
    }


    public void deletePlanner(int planner_id) {
        PlannerEntity planner = plannerRepository.findById(planner_id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));
        plannerRepository.delete(planner);
    }


    public List<PlannerEntity> getPlannersByUser(String user_id) {
        return plannerRepository.findByUser_UserId(user_id);
    }



    public List<LandMarkEntity> recommendByWeather(LocalDate date) {
        // Fetch weather data for the specified date
        WeatherEntity weather = weatherRepository.findById(date)
                .orElseThrow(() -> new RuntimeException("Weather data not found for the specified date"));

        String weatherCondition = weather.getWeather();

        // Recommend landmarks based on weather condition
        if ("Clear".equalsIgnoreCase(weatherCondition)) {
            return landmarkRepository.findByThemeName("Outdoor");
        } else if ("Rain".equalsIgnoreCase(weatherCondition) || "Clouds".equalsIgnoreCase(weatherCondition)) {
            return landmarkRepository.findByThemeName("Indoor");
        } else {
            return landmarkRepository.findAll(); // Default: Recommend all landmarks
        }
    }
}*/
