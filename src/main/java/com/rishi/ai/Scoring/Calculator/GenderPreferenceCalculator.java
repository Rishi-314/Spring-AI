package com.rishi.ai.Scoring.Calculator;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

@Component
public class GenderPreferenceCalculator implements FeatureCalculator {

    @Override
    public FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context) {
        String preferredGender = context.genderPreference();
        String specialistGender = specialist.gender();

        // No preference = matches anyone (1.0)
        if (preferredGender == null || preferredGender.isBlank()) {
            return new FeatureScore(1.0, "No gender preference");
        }

        // No gender data on specialist = neutral (0.5)
        if (specialistGender == null || specialistGender.isBlank()) {
            return new FeatureScore(0.5, "Gender info unavailable");
        }

        // Exact match
        if (specialistGender.equalsIgnoreCase(preferredGender)) {
            return new FeatureScore(1.0, "Gender: " + specialistGender);
        } else {
            return new FeatureScore(0.0, "Gender: " + specialistGender + " (preferred: " + preferredGender + ")");
        }
    }

    @Override
    public String featureName() {
        return "gender_preference";
    }
}
