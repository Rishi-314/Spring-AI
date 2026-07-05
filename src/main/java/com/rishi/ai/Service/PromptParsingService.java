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
          "treatment": "name of treatment/procedure (string or null). Valid options: Angioplasty, Bronchoscopy, Cataract Surgery, Chemotherapy, C-Section, Endoscopy, Joint Injection, Kidney Stone Removal, Knee Replacement, MRI Scan, Psychotherapy, Skin Graft, Tonsillectomy, Ultrasound, Vaccination",
          "budget": "maximum consultation fee in rupees as integer (e.g., 1500). Range: 700-3000 or null for no budget constraint",
          "genderPreference": "M for male, F for female, or null for any gender",
          "languagePreference": "preferred language name (exact match from: English, Hindi, Bengali, Gujarati, Kannada, Malayalam, Punjabi, Tamil, Telugu) or null",
          "availabilityRequired": "true if specialist must be available now, false or null otherwise"
        }
        
        VALIDATION RULES:
        - treatment: MUST match the list exactly (case-sensitive) or be null
        - budget: MUST be a number between 700-3000, not a string, or null
        - genderPreference: MUST be exactly "M", "F", or null (never "Male"/"Female")
        - languagePreference: MUST use exact names from the list above (case-sensitive), or null
        - availabilityRequired: MUST be true/false/null (boolean), never a string
        - If a field is not mentioned in the user query, use null
        
        Examples:
        - "I need chemotherapy with ₹1500 budget" → {"treatment":"Chemotherapy","budget":1500,"genderPreference":null,"languagePreference":null,"availabilityRequired":null}
        - "Female doctor, Hindi speaker, available today" → {"treatment":null,"budget":null,"genderPreference":"F","languagePreference":"Hindi","availabilityRequired":true}
        
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

