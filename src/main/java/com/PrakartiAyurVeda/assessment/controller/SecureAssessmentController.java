package com.PrakartiAyurVeda.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.PrakartiAyurVeda.assessment.dto.UserAssessmentHistoryDto;
import com.PrakartiAyurVeda.assessment.service.AssessmentService;
import org.springframework.http.ResponseEntity;
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
    private final AssessmentService assessmentService; // Inject AssessmentService

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

    @PostMapping("/run-weekly")
    public Map<String, Object> runWeeklyAssessment(@RequestBody List<Answer> answers) {

        // 🔐 Extract authenticated user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found")
                );

        // 🚀 Run secure agentic flow for a weekly plan
        DietPlan dietPlan = agentExecutionService.runFullWeeklyAssessment(user, answers);

        // Get the assessment details
        Assessment assessment = assessmentRepository.findById(dietPlan.getAssessmentId())
                .orElseThrow(() -> new IllegalStateException("Assessment not found"));

        // Return map response with dietPlan and assessment
        Map<String, Object> response = new HashMap<>();
        response.put("dietPlan", dietPlan);
        response.put("assessment", assessment);
        return response;
    }

    @GetMapping("/history")
    public org.springframework.http.ResponseEntity<List<UserAssessmentHistoryDto>> getAssessmentHistory() {
        // 🔐 Extract authenticated user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found")
                );

        List<UserAssessmentHistoryDto> history = assessmentService.getAllAssessmentsAndDietPlansForUser(user.getId());
        return ResponseEntity.ok(history);
    }
}
