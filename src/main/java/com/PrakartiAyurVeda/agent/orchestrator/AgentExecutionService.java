package com.PrakartiAyurVeda.agent.orchestrator;

import com.PrakartiAyurVeda.diet.dto.DietPlanDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.agent.diet.DietRecommendationAgent;
import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.service.AssessmentService;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.service.DietService;
import com.PrakartiAyurVeda.user.entity.User;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentExecutionService {

    private final AgentOrchestrator orchestrator;
    private final AssessmentService assessmentService;
    private final DietService dietService;
    private final DietRecommendationAgent dietRecommendationAgent;

    @Transactional
    public DietPlan runFullAssessment(User user, List<Answer> answers) {

        // Prepare context
        AgentContext context = new AgentContext();
        context.setUser(user);
        context.setAnswers(answers);

        // Run agents
        orchestrator.run(context);

        if (!context.isSafe()) {
            throw new IllegalStateException("Agent pipeline marked context unsafe");
        }

        // Persist assessment
        Assessment savedAssessment =
                assessmentService.createAssessment(user, answers);

        // Persist diet plan
        DietPlanDto dietPlan = context.getDietPlan();

        return dietService.createDietPlan(
                savedAssessment,
                dietPlan.getBreakfast(),
                dietPlan.getLunch(),
                dietPlan.getDinner(),
                dietPlan.getAvoidFoods()
        );
    }

    @Transactional
    public DietPlan regenerateDetailedDietPlan(Long assessmentId) {
        // Get existing assessment with user and answers
        Assessment assessment = assessmentService.getById(assessmentId);

        // Prepare context with user profile details (age, gender, location, foodPreference)
        AgentContext context = new AgentContext();
        context.setUser(assessment.getUser());
        context.setDominantDosha(assessment.getDominantDosha());
        System.out.println("Regenerating diet plan for dominant dosha: " + assessment.getDominantDosha());

        // Generate detailed diet plan directly without full agent pipeline
        DietPlanDto newDietPlan = dietRecommendationAgent.generateDetailedDietPlan(context);
        System.out.println("Regenerated Diet Plan: " + newDietPlan.getBreakfast());

        // Update existing diet plan with new values
        return dietService.updateDietPlan(
                assessmentId,
                newDietPlan.getBreakfast(),
                newDietPlan.getLunch(),
                newDietPlan.getDinner(),
                newDietPlan.getAvoidFoods()
        );
    }
}
