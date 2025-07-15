package com.healthconnect.repository;

// JPA repository for WaterIntake entity
import com.healthconnect.entity.User;
import com.healthconnect.entity.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Extends JpaRepository for CRUD operations on WaterIntake
public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {

    // Finds all water intake records for a given user
    List<WaterIntake> findByUser(User user);

    // Finds water intake records for a user within a date range
    List<WaterIntake> findByUserAndIntakeDateBetween(User user, LocalDate startDate, LocalDate endDate);

    // Finds a water intake record for a specific user and date
    Optional<WaterIntake> findByUserAndIntakeDate(User user, LocalDate intakeDate);

    // Calculates total water intake for a user within a date range
    @Query("SELECT SUM(w.amountLiters) FROM WaterIntake w WHERE w.user = :user AND w.intakeDate BETWEEN :start AND :end")
    Double getTotalWaterIntake(User user, LocalDate start, LocalDate end);
}
