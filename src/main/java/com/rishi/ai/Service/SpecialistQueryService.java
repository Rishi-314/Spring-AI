package com.rishi.ai.Service;


import org.springframework.stereotype.Service;

import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Entity.Specialist;
import com.rishi.ai.Entity.SpecialistTreatment;
import com.rishi.ai.Entity.Treatment;
import com.rishi.ai.Repository.SpecialistRepository;
import com.rishi.ai.Repository.SpecialistTreatmentRepository;
import com.rishi.ai.Repository.TreatmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SpecialistQueryService {

    private final SpecialistRepository specialistRepository;
    private final TreatmentRepository treatmentRepository;
    private final SpecialistTreatmentRepository specialistTreatmentRepository;

    public SpecialistQueryService(SpecialistRepository specialistRepository,
                                   TreatmentRepository treatmentRepository,
                                   SpecialistTreatmentRepository specialistTreatmentRepository) {
        this.specialistRepository = specialistRepository;
        this.treatmentRepository = treatmentRepository;
        this.specialistTreatmentRepository = specialistTreatmentRepository;
    }

    /**
     * Returns all specialists that offer the given treatment (by name).
     * If treatmentName is null/blank, returns all specialists.
     */
    public List<SpecialistDto> findCandidates(String treatmentName) {
        List<Specialist> specialists;

        if (treatmentName == null || treatmentName.isBlank()) {
            specialists = specialistRepository.findAll();
        } else {
            Optional<Treatment> treatment = treatmentRepository.findByTreatmentNameIgnoreCase(treatmentName.trim());

            if (treatment.isEmpty()) {
                return List.of(); // no matching treatment found, no candidates
            }

            List<SpecialistTreatment> links = specialistTreatmentRepository
                    .findByTreatment_TreatmentId(treatment.get().getTreatmentId());

            specialists = links.stream()
                    .map(SpecialistTreatment::getSpecialist)
                    .toList();
        }

        return specialists.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SpecialistDto toDto(Specialist s) {
        List<String> languages = s.getLanguages() == null
                ? List.of()
                : Arrays.stream(s.getLanguages().split(","))
                        .map(String::trim)
                        .toList();

        List<String> treatmentNames = s.getTreatments() == null
                ? List.of()
                : s.getTreatments().stream()
                        .map(st -> st.getTreatment().getTreatmentName())
                        .toList();

        return new SpecialistDto(
                s.getSpecialistId(),
                s.getFirstName(),
                s.getLastName(),
                s.getGender(),
                s.getSpecialization(),
                s.getExperienceYears(),
                s.getConsultationFee(),
                languages,
                s.getRating(),
                s.getTotalReviews(),
                s.getHasAvailability(),
                treatmentNames
        );
    }
}
