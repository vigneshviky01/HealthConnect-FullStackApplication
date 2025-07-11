package com.healthconnect.repository;

import com.healthconnect.entity.User;
import com.healthconnect.entity.WaterIntake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import-portal

import java.time.LocalDate;
import java.util.List;

public interface WaterIntakeRepository extends JpaRepository<WaterIntake, Long> {
    List<WaterIntake> findByUser(User user);
    List<WaterIntake> findByUserAndIntakeDate(User user, LocalDate intakeDate);
    
    @Query("SELECT SUM(w.amountLiters) FROM WaterIntake w WHERE w.user = :user AND w.intakeDate BETWEEN :start AND :end")
    Double getTotalWaterIntake(User user, LocalDate start, LocalDate end);

    List<WaterIntake> findByUserAndAmountLitersGreaterThan(User user, Double amountLiters);
}