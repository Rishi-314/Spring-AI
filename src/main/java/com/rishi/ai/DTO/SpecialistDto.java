package com.rishi.ai.DTO;

import java.math.BigDecimal;
import java.util.List;

public record SpecialistDto(
    Integer specialistId,
    String firstName,
    String lastName,
    String gender,
    String specialization,
    Integer experienceYears,
    BigDecimal consultationFee,
    List<String> languages,
    BigDecimal rating,
    Integer totalReviews,
    Boolean hasAvailability,
    List<String> treatmentNames
) {}