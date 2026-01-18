package com.PrakartiAyurVeda.agent.diet;

import com.PrakartiAyurVeda.agent.diet.dto.AiDietResponse;
import com.PrakartiAyurVeda.diet.dto.DietPlanDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.PrakartiAyurVeda.agent.Agent;
import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.common.enums.DoshaType;

@Component
@Order(3)
public class DietRecommendationAgent implements Agent {

    private final ChatClient chatClient;
    private final SimpleLoggerAdvisor simpleLoggerAdvisor;

    public DietRecommendationAgent(ChatClient.Builder builder, SimpleLoggerAdvisor simpleLoggerAdvisor) {
        this.chatClient = builder.defaultAdvisors(simpleLoggerAdvisor).build();
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
    }

    @Override
    public void execute(AgentContext context) {

        DoshaType dosha = context.getDominantDosha();

        if (dosha == null) {
            context.setSafe(false);
            throw new IllegalStateException("Dosha not determined before diet generation");
        }

        boolean useDetailed = context.isGenerateWeeklyPlan();
        DietPlanDto dietPlan = generateDietPlan(context, dosha, useDetailed);
        context.setDietPlan(dietPlan);
    }

    /**
     * Generate diet plan directly without full agent pipeline
     * Useful for regenerating diet plans for existing assessments
     */
    public DietPlanDto generateDetailedDietPlan(AgentContext context) {
        DoshaType dosha = context.getDominantDosha();

        if (dosha == null) {
            throw new IllegalStateException("Dosha not determined before diet generation");
        }

        return generateDietPlan(context, dosha, true);
    }

    private DietPlanDto generateDietPlan(AgentContext context, DoshaType dosha, boolean useDetailed) {
        String prompt = useDetailed ? buildDetailedPrompt(context) : buildSimplePrompt(dosha);

        try {
            AiDietResponse aiDietResponse = chatClient
                    .prompt()
                    .advisors(simpleLoggerAdvisor)
                    .user(prompt)
                    .call()
                    .entity(AiDietResponse.class);

            assert aiDietResponse != null;
            return aiDietResponse.toDietPlanDto(dosha);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate or parse diet plan from AI response", e);
        }
    }

    /**
     * Simple prompt for regular assessment
     */
    private String buildSimplePrompt(DoshaType dosha) {
        return """
            You are a certified Ayurveda diet expert.

            User details:
            - Dominant Dosha: %s
            - Diet type: Indian vegetarian
            - Goal: General health & balance

            Generate a DAILY diet plan in STRICT JSON format:

            {
              "weeklyDiet": [{
                  "dayOfWeek": "MONDAY",
                  "breakfast": "...",
                  "lunch": "...",
                  "dinner": "..."
              }],
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
            - dayOfWeek should be MONDAY
            """
                .formatted(dosha.name());
    }

    /**
     * Detailed prompt with user profile for regenerate API
     */
    private String buildDetailedPrompt(AgentContext context) {
        DoshaType dosha = context.getDominantDosha();

        // Get user details with defaults
        String foodPreference = "vegetarian";
        String age = "not specified";
        String gender = "not specified";
        String location = "India";

        if (context.getUser() != null) {
            if (context.getUser().getFoodPreference() != null) {
                foodPreference = context.getUser().getFoodPreference().name().toLowerCase().replace("_", " ");
            }
            if (context.getUser().getAge() != null) {
                age = context.getUser().getAge().toString();
            }
            if (context.getUser().getGender() != null) {
                gender = context.getUser().getGender();
            }
            if (context.getUser().getLocation() != null) {
                location = context.getUser().getLocation();
            }
        }

        return """
            You are a certified Ayurveda diet expert.

            User details:
            - Dominant Dosha: %s
            - Diet type: Indian %s
            - Age: %s
            - Gender: %s
            - Location: %s
            - Goal: General health & balance

            Generate a DETAILED 7-DAY (WEEKLY) diet plan in STRICT JSON format with specific meal suggestions:

            {
              "weeklyDiet": [
                {
                  "dayOfWeek": "MONDAY",
                  "breakfast": "Detailed breakfast with portions and timing",
                  "lunch": "Detailed lunch with portions and timing",
                  "dinner": "Detailed dinner with portions and timing"
                },
                {
                  "dayOfWeek": "TUESDAY",
                  "breakfast": "...",
                  "lunch": "...",
                  "dinner": "..."
                }
              ],
              "avoidFoods": "Specific foods to avoid based on dosha and preferences"
            }

            Rules:
            - No medical claims
            - No extreme fasting
            - Use %s diet strictly (no animal products if vegetarian/vegan)
            - Consider location and seasonal availability
            - Age and gender appropriate portions
            - Cooling foods for PITTA
            - Warming foods for VATA
            - Light foods for KAPHA
            - Provide specific portion sizes and meal timings
            - Include traditional Indian preparations
            - JSON ONLY, no explanation
            """
                .formatted(dosha.name(), foodPreference, age, gender, location, foodPreference);
    }
}
