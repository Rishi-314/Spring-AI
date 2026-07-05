package com.rishi.ai.Scoring.Calculator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

@Component
public class RatingCalculator implements FeatureCalculator {

    @Override
    public FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context) {
        BigDecimal rating = specialist.rating();
        Integer reviewCount = specialist.totalReviews();

        // No rating data = neutral
        if (rating == null) {
            return new FeatureScore(0.5, "No ratings yet");
        }

        // If 0 reviews with 0 rating (edge case from your schema), treat as unrated
        if (reviewCount != null && reviewCount == 0 && rating.compareTo(BigDecimal.ZERO) == 0) {
            return new FeatureScore(0.5, "Unrated (0 reviews)");
        }

        // Normalize rating: assume 5.0 is max
        double normalizedScore = Math.min(rating.doubleValue() / 5.0, 1.0);
        String reason = rating + "/5.0 (" + reviewCount + " reviews)";

        return new FeatureScore(normalizedScore, reason);
    }

    @Override
    public String featureName() {
        return "rating";
    }
}
