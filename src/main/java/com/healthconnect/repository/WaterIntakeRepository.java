package com.healthconnect.repository;

import com.healthconnect.entity.User;
import com.healthconnect.entity.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;

//import-portal

import java.time.LocalDate;
import java.util.List;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
    List<WaterIntake> findByUser(User user);
    List<WaterIntake> findByUserAndIntakeDateBetween(User user, LocalDate startDate, LocalDate endDate);
}