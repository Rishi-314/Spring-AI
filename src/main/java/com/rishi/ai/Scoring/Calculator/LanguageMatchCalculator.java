package com.rishi.ai.Scoring.Calculator;

import org.springframework.stereotype.Component;

import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.Entity.FeatureScore;

import java.util.List;

@Component
public class LanguageMatchCalculator implements FeatureCalculator {

    @Override
    public FeatureScore calculate(SpecialistDto specialist, ScoringContextDto context) {
        String preferredLanguage = context.languagePreference();
        List<String> specialistLanguages = specialist.languages();

        // No preference = matches anyone (1.0)
        if (preferredLanguage == null || preferredLanguage.isBlank()) {
            return new FeatureScore(1.0, "No language preference");
        }

        // No language data = neutral (0.5)
        if (specialistLanguages == null || specialistLanguages.isEmpty()) {
            return new FeatureScore(0.5, "Language info unavailable");
        }

        // Check if any of specialist's languages match (case-insensitive)
        boolean matches = specialistLanguages.stream()
                .anyMatch(lang -> lang.equalsIgnoreCase(preferredLanguage.trim()));

        if (matches) {
            return new FeatureScore(1.0, "Speaks " + preferredLanguage);
        } else {
            return new FeatureScore(0.0, "Does not speak " + preferredLanguage);
        }
    }

    @Override
    public String featureName() {
        return "language_match";
    }
}
