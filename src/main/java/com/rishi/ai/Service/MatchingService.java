package com.rishi.ai.Service;

import java.math.BigDecimal;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import com.rishi.ai.DTO.ScoringContextDto;
import com.rishi.ai.DTO.SpecialistDto;
import com.rishi.ai.Scoring.WeightedScorer;
import com.rishi.ai.Scoring.Entity.RankedDoctor;

import java.util.List;
import java.util.Map;

@Service
public class MatchingService {

    private static final Logger logger = LoggerFactory.getLogger(MatchingService.class);

    private final PromptParsingService promptParsingService;
    private final SpecialistQueryService specialistQueryService;
    private final WeightsQueryService weightsQueryService;
    private final WeightedScorer weightedScorer;

    public MatchingService(PromptParsingService promptParsingService,
                           SpecialistQueryService specialistQueryService,
                           WeightsQueryService weightsQueryService,
                           WeightedScorer weightedScorer) {
        this.promptParsingService = promptParsingService;
        this.specialistQueryService = specialistQueryService;
        this.weightsQueryService = weightsQueryService;
        this.weightedScorer = weightedScorer;
    }

    /**
     * Main matching pipeline:
     * 1. Parse user's free-text prompt into structured context
     * 2. Fetch live weights from DB
     * 3. Query specialists (filtered by treatment if specified)
     * 4. Score and rank them
     * 5. Return ranked results
     */
    public List<RankedDoctor> matchSpecialists(String userPrompt) {
        logger.info("Starting specialist matching for prompt: {}", userPrompt);

        // Step 1: Parse the prompt
        ScoringContextDto context = promptParsingService.parseUserPrompt(userPrompt);
        logger.info("Parsed context: {}", context);

        // Step 2: Fetch live weights
        Map<String, BigDecimal> weights = weightsQueryService.getLiveWeights();
        logger.info("Loaded weights: {}", weights);

        // Step 3: Query candidates (filtered by treatment if specified)
        String treatmentFilter = context.treatment();
        List<SpecialistDto> candidates = specialistQueryService.findCandidates(treatmentFilter);
        logger.info("Found {} specialist candidates for treatment: {}", candidates.size(), treatmentFilter);

        if (candidates.isEmpty()) {
            logger.warn("No candidates found for treatment: {}", treatmentFilter);
            return List.of();
        }

        // Step 4: Score and rank
        List<RankedDoctor> ranked = weightedScorer.rankCandidates(candidates, context, weights);
        logger.info("Ranked {} specialists", ranked.size());

        return ranked;
    }
}
