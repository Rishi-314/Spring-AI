package com.rishi.ai.Scoring.Entity;

public record FeatureScore(
    double score,      // normalized to 0.0-1.0
    String reason      // explanation, e.g. "2 years exp, mid-range"
) {}