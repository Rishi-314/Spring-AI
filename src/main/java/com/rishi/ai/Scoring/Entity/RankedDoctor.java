package com.rishi.ai.Scoring.Entity;
import java.util.List;

public record RankedDoctor(
    Integer specialistId,
    String fullName,
    double finalScore,
    List<ScoredFeature> breakdown,
    List<String> matchFacts  // e.g. ["Offers Chemotherapy", "Within budget", "Available"]
) {}
