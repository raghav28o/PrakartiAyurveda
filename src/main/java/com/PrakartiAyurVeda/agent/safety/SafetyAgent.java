package com.PrakartiAyurVeda.agent.safety;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.PrakartiAyurVeda.agent.Agent;
import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.diet.dto.DietPlanDto;

@Component
@Order(4)
public class SafetyAgent implements Agent {

    @Override
    public void execute(AgentContext context) {

        DietPlanDto dietPlan = context.getDietPlan();

        if (dietPlan == null) {
            context.setSafe(false);
            throw new IllegalStateException("Diet plan missing — unsafe to continue");
        }

        // Mandatory fields check
        if (isBlank(dietPlan.getBreakfast())
                || isBlank(dietPlan.getLunch())
                || isBlank(dietPlan.getDinner())) {

            context.setSafe(false);
            throw new IllegalStateException("Incomplete diet plan generated");
        }

        // Basic safety keyword checks
        String combined = (
                dietPlan.getBreakfast()
                        + dietPlan.getLunch()
                        + dietPlan.getDinner()
                        + dietPlan.getAvoidFoods()
        ).toLowerCase();

        if (containsUnsafeTerms(combined)) {
            context.setSafe(false);
            throw new IllegalStateException("Unsafe dietary content detected");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean containsUnsafeTerms(String text) {
        return text.contains("fasting")
                || text.contains("starve")
                || text.contains("medicine")
                || text.contains("cure")
                || text.contains("treatment");
    }
}
