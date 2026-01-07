package com.PrakartiAyurVeda.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.PrakartiAyurVeda.agent.orchestrator.AgentExecutionService;
import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.repository.AssessmentRepository;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assessment")
@RequiredArgsConstructor
public class SecureAssessmentController {

    private final AgentExecutionService agentExecutionService;
    private final UserRepository userRepository;
    private final AssessmentRepository assessmentRepository;

    @PostMapping("/run")
    public Map<String, Object> runAssessment(@RequestBody List<Answer> answers) {

        // 🔐 Extract authenticated user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found")
                );

        // 🚀 Run secure agentic flow
        DietPlan dietPlan = agentExecutionService.runFullAssessment(user, answers);

        // Get the assessment details
        Assessment assessment = assessmentRepository.findById(dietPlan.getAssessmentId())
                .orElseThrow(() -> new IllegalStateException("Assessment not found"));

        // Return map response with dietPlan and assessment
        Map<String, Object> response = new HashMap<>();
        response.put("dietPlan", dietPlan);
        response.put("assessment", assessment);
        return response;
    }
}
