package com.rishi.ai.Scoring.Calculator;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

@Component
public class ExperienceCalculator implements FeatureCalculator {

    @Override
    public FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context) {
        Integer years = specialist.experienceYears();
        
        if (years == null || years < 0) {
            return new FeatureScore(0.0, "No experience data");
        }

        // Normalize: assume 20+ years = max score (1.0), 0 years = 0.0
        double normalizedScore = Math.min(years / 20.0, 1.0);
        String reason = years + " years experience";

        return new FeatureScore(normalizedScore, reason);
    }

    @Override
    public String featureName() {
        return "experience";
    }
}
