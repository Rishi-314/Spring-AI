package com.rishi.ai.Scoring.Entity;

public record ScoredFeature(
    String featureName,
    double rawScore,
    double weight,
    double weightedContribution,
    String reason
) {}
