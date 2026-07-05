package com.rishi.ai.Repository;


import com.rishi.ai.Entity.SpecialistTreatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialistTreatmentRepository extends JpaRepository<SpecialistTreatment, SpecialistTreatment.SpecialistTreatmentId> {
    List<SpecialistTreatment> findByTreatment_TreatmentId(Integer treatmentId);
}