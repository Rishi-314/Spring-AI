package com.rishi.ai.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rishi.ai.DTO.ScoringContextDto;


@Service
public class PromptParsingService {

    private static final Logger logger = LoggerFactory.getLogger(PromptParsingService.class);
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public PromptParsingService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public ScoringContextDto parseUserPrompt(String userPrompt) {
        String systemPrompt = """
        You are a healthcare specialist matcher. Extract the following from the user's query and respond ONLY with valid JSON (no other text).
        
        JSON format with EXACT field values:
        {
          "treatment": "name of treatment/procedure EXACTLY as mentioned by user, matched against valid options: Angioplasty, Bronchoscopy, Cataract Surgery, Chemotherapy, C-Section, Endoscopy, Joint Injection, Kidney Stone Removal, Knee Replacement, MRI Scan, Psychotherapy, Skin Graft, Tonsillectomy, Ultrasound, Vaccination. If user mentions a treatment (even with different wording), find the closest match from the list. If NO treatment mentioned, use null",
          "budget": "maximum consultation fee in rupees as integer (e.g., 1500). Range: 700-3000 or null for no budget constraint",
          "genderPreference": "preferred gender (exact match from: Female, Male) or null for any gender",
          "languagePreference": "preferred language code (exact match from: en, hi, bn, gu, kn, ml, pa, ta, te) or null. Examples: en=English, hi=Hindi, ta=Tamil",
          "availabilityRequired": "true if specialist must be available now, false or null otherwise"
        }
        
        VALIDATION RULES:
        - treatment: MUST match the list exactly (case-sensitive) or be null
        - budget: MUST be a number between 700-3000, not a string, or null
        - genderPreference: MUST be exactly "Female", "Male", or null (full words, case-sensitive)
        - languagePreference: MUST be exactly "en", "hi", "bn", "gu", "kn", "ml", "pa", "ta", "te", or null
        - availabilityRequired: MUST be true/false/null (boolean), never a string
        - If a field is not mentioned in the user query, use null
        
        Examples:
        - "I need chemotherapy with ₹1500 budget, female doctor" → {"treatment":"Chemotherapy","budget":1500,"genderPreference":"Female","languagePreference":null,"availabilityRequired":null}
        - "Female doctor, Hindi speaker, available today" → {"treatment":null,"budget":null,"genderPreference":"Female","languagePreference":"hi","availabilityRequired":true}
        
        Respond ONLY with the JSON object, no markdown backticks, no explanation.
                """;

        String response = chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();

        logger.info("LLM parsed prompt. Raw response: {}", response);

        try {
            String cleanedResponse = response
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            ScoringContextDto dto = objectMapper.readValue(cleanedResponse, ScoringContextDto.class);
            logger.info("Successfully parsed into DTO: {}", dto);
            return dto;

        } catch (Exception e) {
            logger.error("Failed to parse LLM response as JSON: {}", response, e);
            return new ScoringContextDto(null, null, null, null, null);
        }
    }
}

