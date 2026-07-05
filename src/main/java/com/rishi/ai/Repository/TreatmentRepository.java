package com.rishi.ai.Repository;


import com.rishi.ai.Entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Integer> {
    Optional<Treatment> findByTreatmentNameIgnoreCase(String treatmentName);
}