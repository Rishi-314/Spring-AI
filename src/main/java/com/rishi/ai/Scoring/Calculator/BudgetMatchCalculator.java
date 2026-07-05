package com.rishi.ai.Scoring.Calculator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

@Component
public class BudgetMatchCalculator implements FeatureCalculator {

    @Override
    public FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context) {
        BigDecimal fee = specialist.consultationFee();
        Integer budget = context.budget();

        // No budget specified = matches anyone (score 1.0)
        if (budget == null) {
            return new FeatureScore(1.0, "No budget constraint");
        }

        // No fee info = neutral (score 0.5)
        if (fee == null) {
            return new FeatureScore(0.5, "Fee information unavailable");
        }

        // If fee <= budget, perfect match (1.0)
        if (fee.compareTo(BigDecimal.valueOf(budget)) <= 0) {
            return new FeatureScore(1.0, "₹" + fee + " within budget ₹" + budget);
        }

        // If fee > budget, score inversely proportional to overage
        // e.g., budget 1500, fee 2000 = 25% over = 0.75 score
        double overage = fee.doubleValue() / budget;
        double normalizedScore = Math.max(0.0, 2.0 - overage); // clamps at 0.0
        String reason = "₹" + fee + " exceeds budget ₹" + budget;

        return new FeatureScore(normalizedScore, reason);
    }

    @Override
    public String featureName() {
        return "budget_match";
    }
}
