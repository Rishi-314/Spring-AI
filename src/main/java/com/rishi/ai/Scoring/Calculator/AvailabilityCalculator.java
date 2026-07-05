package com.rishi.ai.Scoring.Calculator;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

@Component
public class AvailabilityCalculator implements FeatureCalculator {

    @Override
    public FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context) {
        Boolean required = context.availabilityRequired();
        Boolean available = specialist.hasAvailability();

        // Availability not required = full score regardless (1.0)
        if (required == null || !required) {
            return new FeatureScore(1.0, "Availability not required");
        }

        // Availability required, but no data = neutral (0.5)
        if (available == null) {
            return new FeatureScore(0.5, "Availability unknown");
        }

        // Exact match
        if (available) {
            return new FeatureScore(1.0, "Available now");
        } else {
            return new FeatureScore(0.0, "Not currently available");
        }
    }

    @Override
    public String featureName() {
        return "availability";
    }
}
