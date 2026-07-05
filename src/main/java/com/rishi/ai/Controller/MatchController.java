package com.rishi.ai.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rishi.ai.DTO.MatchResponse;
import com.rishi.ai.Scoring.Entity.RankedDoctor;
import com.rishi.ai.Service.MatchingService;

@RestController
public class MatchController {

    private final MatchingService matchingService;

    public MatchController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /**
     * Match specialists based on user's natural language prompt.
     * 
     * Example: /match?prompt=I+need+chemotherapy+with+budget+1500
     * 
     * Returns ranked list of specialists with full score breakdowns.
     */
    @GetMapping("/match")
    public List<MatchResponse> matchSpecialists(@RequestParam String prompt) {
        List<RankedDoctor> ranked = matchingService.matchSpecialists(prompt);

        return ranked.stream()
                .map(MatchResponse::fromRankedDoctor)
                .toList();
    }
}
