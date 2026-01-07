package com.PrakartiAyurVeda.agent.diet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper;

    public DietRecommendationAgent(ChatClient.Builder builder, SimpleLoggerAdvisor simpleLoggerAdvisor, ObjectMapper objectMapper) {
        this.chatClient = builder.defaultAdvisors(simpleLoggerAdvisor).build();
        this.simpleLoggerAdvisor = simpleLoggerAdvisor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void execute(AgentContext context) {

        DoshaType dosha = context.getDominantDosha();

        if (dosha == null) {
            context.setSafe(false);
            throw new IllegalStateException("Dosha not determined before diet generation");
        }

        DietPlanDto dietPlan = generateDietPlan(context, dosha, false);
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

        String aiResponse = chatClient
                .prompt()
                .advisors(simpleLoggerAdvisor)
                .user(prompt)
                .call()
                .content();

        return parseDiet(aiResponse, dosha);
    }

    /**
     * Prompt engineering happens here
     */
    private String buildPrompt(AgentContext context) {
        if(context.isSetUseDetailedDietPlan()) {
            return buildDetailedPrompt(context);
        }
        return buildSimplePrompt(context.getDominantDosha());
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

            Generate a DETAILED DAILY diet plan in STRICT JSON format with specific meal suggestions:

            {
              "breakfast": "Detailed breakfast with portions and timing",
              "lunch": "Detailed lunch with portions and timing",
              "dinner": "Detailed dinner with portions and timing",
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

    /**
     * Parse diet plan from AI response (handles nested JSON and metadata-wrapped responses)
     */
    private DietPlanDto parseDiet(String aiResponse, DoshaType dosha) {
        DietPlanDto dietPlan = new DietPlanDto();
        dietPlan.setDoshaType(dosha);

        try {
            // First, try to extract JSON content from markdown code blocks if present
            String jsonContent = extractJsonFromResponse(aiResponse);

            // Parse the JSON
            JsonNode root = objectMapper.readTree(jsonContent);

            // Extract breakfast
            dietPlan.setBreakfast(extractMealAsString(root, "breakfast"));

            // Extract lunch
            dietPlan.setLunch(extractMealAsString(root, "lunch"));

            // Extract dinner
            dietPlan.setDinner(extractMealAsString(root, "dinner"));

            // Extract avoid foods
            dietPlan.setAvoidFoods(extractAvoidFoods(root));

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse diet plan from AI response: " + e.getMessage(), e);
        }

        return dietPlan;
    }

    /**
     * Extract JSON from response, handling markdown code blocks and nested structures
     */
    private String extractJsonFromResponse(String response) {
        String textContent = response;

        // If response is a JSON object, try to extract the text field
        try {
            JsonNode responseNode = objectMapper.readTree(response);
            // Try to navigate to the text field in the nested response structure
            JsonNode textNode = responseNode.at("/results/0/output/text");
            if (textNode != null && !textNode.isMissingNode()) {
                textContent = textNode.asText();
            }
        } catch (Exception e) {
            // Not a JSON object, continue with original response
        }

        // If response contains markdown code block, extract JSON from it
        if (textContent.contains("```json")) {
            int start = textContent.indexOf("```json") + 7;
            int end = textContent.indexOf("```", start);
            if (end != -1) {
                return textContent.substring(start, end).trim();
            }
        }

        // Otherwise return as is
        return textContent.trim();
    }

    /**
     * Extract meal information and convert to string representation
     * Handles both simple string format and complex nested object format
     */
    private String extractMealAsString(JsonNode root, String mealType) {
        JsonNode mealNode = root.get(mealType);

        if (mealNode == null) {
            return "";
        }

        // If it's a simple string, return it
        if (mealNode.isTextual()) {
            return mealNode.asText();
        }

        // If it's a complex object, format it nicely
        if (mealNode.isObject()) {
            StringBuilder sb = new StringBuilder();

            // Extract item (the dish name) - try both "item" and "meal" fields
            String itemLabel = mealNode.has("item") ? "item" : "meal";
            if (mealNode.has(itemLabel)) {
                sb.append(mealNode.get(itemLabel).asText()).append("\n");
            }

            // Extract portions (singular or plural)
            String portionLabel = mealNode.has("portions") ? "portions" : "portion";
            if (mealNode.has(portionLabel)) {
                JsonNode portions = mealNode.get(portionLabel);
                if (portions.isObject()) {
                    // Format as key-value pairs
                    portions.fieldNames().forEachRemaining(ingredient -> {
                        sb.append("  ").append(ingredient).append(": ").append(portions.get(ingredient).asText()).append("\n");
                    });
                } else if (portions.isArray()) {
                    // If array, join with comma
                    boolean first = true;
                    for (JsonNode portion : portions) {
                        if (!first) sb.append(", ");
                        sb.append(portion.asText());
                        first = false;
                    }
                    sb.append("\n");
                } else if (portions.isTextual()) {
                    sb.append(portions.asText()).append("\n");
                }
            }

            // Extract ingredients (can be array or object)
            if (mealNode.has("ingredients")) {
                sb.append("Ingredients:\n");
                JsonNode ingredients = mealNode.get("ingredients");
                if (ingredients.isArray()) {
                    // If array, list each item
                    for (JsonNode ingredient : ingredients) {
                        sb.append("  - ").append(ingredient.asText()).append("\n");
                    }
                } else if (ingredients.isObject()) {
                    // If object, format as key-value pairs
                    ingredients.fieldNames().forEachRemaining(ingredient ->
                        sb.append("  - ").append(ingredient).append(": ").append(ingredients.get(ingredient).asText()).append("\n")
                    );
                }
            }

            // Extract timing
            if (mealNode.has("timing")) {
                sb.append("Timing: ").append(mealNode.get("timing").asText());
            }

            // Extract preparation if present
            if (mealNode.has("preparation")) {
                sb.append("Preparation: ").append(mealNode.get("preparation").asText());
            }

            return sb.toString().trim();
        }

        return "";
    }

    /**
     * Extract avoid foods list
     */
    private String extractAvoidFoods(JsonNode root) {
        JsonNode avoidNode = root.get("avoidFoods");

        if (avoidNode == null) {
            return "";
        }

        // If it's a string, return it
        if (avoidNode.isTextual()) {
            return avoidNode.asText();
        }

        // If it's an array, join with commas
        if (avoidNode.isArray()) {
            StringBuilder sb = new StringBuilder();
            avoidNode.forEach(item -> {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(item.asText());
            });
            return sb.toString();
        }

        return "";
    }
}
