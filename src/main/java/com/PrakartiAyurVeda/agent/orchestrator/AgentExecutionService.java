package com.PrakartiAyurVeda.agent.orchestrator;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.service.AssessmentService;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.service.DietService;
import com.PrakartiAyurVeda.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentExecutionService {

    private final AgentOrchestrator orchestrator;
    private final AssessmentService assessmentService;
    private final DietService dietService;

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
        DietPlan dietPlan = context.getDietPlan();
        dietPlan.setAssessment(savedAssessment);

        return dietService.createDietPlan(
                savedAssessment,
                dietPlan.getBreakfast(),
                dietPlan.getLunch(),
                dietPlan.getDinner(),
                dietPlan.getAvoidFoods()
        );
    }
}
