package com.rishi.ai.Scoring.Calculator;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

public interface FeatureCalculator {
    /**
     * Calculate a normalized score (0.0-1.0) for this feature,
     * given the specialist and the user's requirements.
     */
    FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context);

    /**
     * Returns the name of this feature (e.g. "experience", "budget_match").
     * Must match keys in weights_config table.
     */
    String featureName();
}
