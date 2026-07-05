package com.rishi.ai.DTO;

import java.util.List;

import com.rishi.ai.Scoring.Entity.RankedDoctor;

public record MatchResponse(
    Integer specialistId,
    String fullName,
    double matchScore,
    List<FeatureBreakdown> scoreBreakdown,
    List<String> whyThisSpecialist
) {
    public static MatchResponse fromRankedDoctor(RankedDoctor ranked) {
        List<FeatureBreakdown> breakdown = ranked.breakdown().stream()
                .map(sf -> new FeatureBreakdown(
                        sf.featureName(),
                        sf.rawScore(),
                        sf.weight(),
                        sf.weightedContribution(),
                        sf.reason()
                ))
                .toList();

        return new MatchResponse(
                ranked.specialistId(),
                ranked.fullName(),
                ranked.finalScore(),
                breakdown,
                ranked.matchFacts()
        );
    }

    public record FeatureBreakdown(
        String featureName,
        double rawScore,
        double weight,
        double weightedContribution,
        String reason
    ) {}
}
