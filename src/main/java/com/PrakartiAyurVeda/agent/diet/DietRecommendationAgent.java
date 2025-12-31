package com.PrakartiAyurVeda.agent.diet;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.PrakartiAyurVeda.agent.Agent;
import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.diet.entity.DietPlan;

import lombok.RequiredArgsConstructor;

@Component
@Order(3)
@RequiredArgsConstructor
public class DietRecommendationAgent implements Agent {

    private final ChatClient chatClient;

    @Override
    public void execute(AgentContext context) {

        DoshaType dosha = context.getDominantDosha();

        if (dosha == null) {
            context.setSafe(false);
            throw new IllegalStateException("Dosha not determined before diet generation");
        }

        String prompt = buildPrompt(dosha);
        System.out.println("AI_Prompt: "+prompt);

        String aiResponse = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        System.out.println("Ai_Response: "+aiResponse);

        DietPlan dietPlan = parseDiet(aiResponse, dosha);
        context.setDietPlan(dietPlan);
    }

    /**
     * Prompt engineering happens here
     */
    private String buildPrompt(DoshaType dosha) {

        return """
            You are a certified Ayurveda diet expert.

            User details:
            - Dominant Dosha: %s
            - Diet type: Indian vegetarian
            - Goal: General health & balance

            Generate a DAILY diet plan in STRICT JSON format:

            {
              "breakfast": "...",
              "lunch": "...",
              "dinner": "...",
              "avoidFoods": "..."
            }

            Rules:
            - No medical claims
            - No extreme fasting
            - Simple Indian foods
            - Cooling foods for PITTA
            - Warming foods for VATA
            - Light foods for KAPHA
            - JSON ONLY, no explanation
            """
                .formatted(dosha.name());
    }

    /**
     * Temporary simple parsing
     * (will improve in next step)
     */
    private DietPlan parseDiet(String aiResponse, DoshaType dosha) {

        DietPlan dietPlan = new DietPlan();
        dietPlan.setDoshaType(dosha);

        // ⚠️ TEMP parsing (Phase 7.5 we improve this)
        dietPlan.setBreakfast(extract(aiResponse, "breakfast"));
        dietPlan.setLunch(extract(aiResponse, "lunch"));
        dietPlan.setDinner(extract(aiResponse, "dinner"));
        dietPlan.setAvoidFoods(extract(aiResponse, "avoidFoods"));

        return dietPlan;
    }

    private String extract(String json, String field) {
        int start = json.indexOf("\"" + field + "\"");
        if (start == -1) return "";
        start = json.indexOf(":", start) + 1;
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return json.substring(start, end).replaceAll("[\"{}]", "").trim();
    }
}
