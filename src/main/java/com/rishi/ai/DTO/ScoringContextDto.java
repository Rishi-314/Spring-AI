package com.rishi.ai.DTO;

public record ScoringContextDto(
    String treatment,
    Integer budget,
    String genderPreference,
    String languagePreference,
    Boolean availabilityRequired
) {}