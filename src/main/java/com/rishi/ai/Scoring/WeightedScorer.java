package com.rishi.ai.Scoring;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Calculator.FeatureCalculator;
import com.rishi.ai.Scoring.Entity.FeatureScore;
import com.rishi.ai.Scoring.Entity.RankedDoctor;
import com.rishi.ai.Scoring.Entity.ScoredFeature;

@Component
public class WeightedScorer {

    private final List<FeatureCalculator> calculators;

    public WeightedScorer(List<FeatureCalculator> calculators) {
        this.calculators = calculators;
    }

    /**
     * Score a single specialist against the user's context.
     * Returns a RankedDoctor with full breakdown of how the score was computed.
     */
    public RankedDoctor score(SpecialistDto specialist, ScoringContextDto context, Map<String, BigDecimal> weights) {
        List<ScoredFeature> breakdown = new ArrayList<>();
        double finalScore = 0.0;

        for (FeatureCalculator calculator : calculators) {
            FeatureScore result = calculator.calculate(specialist, context);
            String featureName = calculator.featureName();

            // Get weight for this feature (default to 0.0 if missing)
            BigDecimal weightBigDecimal = weights.getOrDefault(featureName, BigDecimal.ZERO);
            double weight = weightBigDecimal.doubleValue();

            // Weighted contribution
            double weightedContribution = result.score() * weight;
            finalScore += weightedContribution;

            breakdown.add(new ScoredFeature(
                    featureName,
                    result.score(),
                    weight,
                    weightedContribution,
                    result.reason()
            ));
        }

        // Build match facts (human-readable explanation of key match points)
        List<String> matchFacts = buildMatchFacts(specialist, breakdown);

        String fullName = specialist.firstName() + " " + specialist.lastName();

        return new RankedDoctor(
                specialist.specialistId(),
                fullName,
                finalScore,
                breakdown,
                matchFacts
        );
    }

    /**
     * Rank all candidates (specialists) by score, highest first.
     */
    public List<RankedDoctor> rankCandidates(List<SpecialistDto> candidates, ScoringContextDto context, Map<String, BigDecimal> weights) {
        return candidates.stream()
                .map(candidate -> score(candidate, context, weights))
                .sorted(Comparator.comparingDouble(RankedDoctor::finalScore).reversed())
                .toList();
    }

    /**
     * Build human-readable match facts from the scored features.
     * Highlights the strongest matches and any critical misses.
     */
    private List<String> buildMatchFacts(SpecialistDto specialist, List<ScoredFeature> breakdown) {
        List<String> facts = new ArrayList<>();

        // Add treatment offerings
        if (specialist.treatmentNames() != null && !specialist.treatmentNames().isEmpty()) {
            facts.add("Offers: " + String.join(", ", specialist.treatmentNames()));
        }

        // Highlight high-scoring features
        for (ScoredFeature feature : breakdown) {
            if (feature.rawScore() >= 0.9) {
                facts.add(featureHighlight(feature));
            }
        }

        // Highlight critical misses (0.0 scores for required features)
        for (ScoredFeature feature : breakdown) {
            if (feature.rawScore() == 0.0 && feature.weight() > 0) {
                facts.add("⚠ " + feature.reason());
            }
        }

        return facts;
    }

    private String featureHighlight(ScoredFeature feature) {
        return switch (feature.featureName()) {
            case "experience" -> "✓ Highly experienced (" + feature.reason() + ")";
            case "budget_match" -> "✓ Within budget (" + feature.reason() + ")";
            case "rating" -> "✓ Well-rated (" + feature.reason() + ")";
            case "language_match" -> "✓ " + feature.reason();
            case "gender_preference" -> "✓ " + feature.reason();
            case "availability" -> "✓ " + feature.reason();
            default -> "✓ " + feature.reason();
        };
    }
}
