/*package com.locationbase.Service;

import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Domain.Repository.PlannerRepository;
import com.locationbase.Domain.Repository.UserRepository;
import com.locationbase.Domain.Repository.WeatherRepository;
import com.locationbase.Entity.UserEntity;
import com.locationbase.Entity.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PlannerService {

    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final WeatherRepository weatherRepository;

    @Autowired
    public PlannerService(PlannerRepository plannerRepository, UserRepository userRepository, WeatherRepository weatherRepository) {
        this.plannerRepository = plannerRepository;
        this.userRepository = userRepository;
        this.weatherRepository = weatherRepository;
    }

    public void savePlanner(int plannerId, String userId) {
        // userId로 UserEntity를 찾음
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 현재 날짜로 WeatherEntity를 찾음
        LocalDate currentDate = LocalDate.now();
        WeatherEntity weather = weatherRepository.findById(currentDate).orElseThrow(() -> new RuntimeException("Weather not found for today"));

        PlannerEntity planner = new PlannerEntity();
        planner.setPlannerId(plannerId);
        planner.setUserId(user); // UserEntity를 설정
        planner.setDate(currentDate); // 현재 날짜로 설정

        plannerRepository.save(planner);
    }
}
*/