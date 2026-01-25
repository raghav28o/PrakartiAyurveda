package com.PrakartiAyurVeda.assessment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PrakartiAyurVeda.agent.orchestrator.AgentExecutionService;
import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.repository.AssessmentRepository;
import com.PrakartiAyurVeda.assessment.service.AssessmentService;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final UserService userService;
    private final AgentExecutionService agentExecutionService;
    private final AssessmentRepository assessmentRepository;

    /**
     * TEMP API
     * Later this will come from JWT-authenticated user
     */
    @PostMapping("/user/{userId}")
    public Assessment createAssessment(
            @PathVariable Long userId,
            @RequestBody List<Answer> answers) {

        User user = userService.getById(userId);
        return assessmentService.createAssessment(user, answers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        Assessment assessment = assessmentService.getById(id);
        return ResponseEntity.ok(assessment);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Assessment>> getAssessmentsByUserId(@PathVariable Long userId) {
        List<Assessment> assessments = assessmentService.getByUserId(userId);
        return ResponseEntity.ok(assessments);
    }

    @PutMapping("/{assessmentId}/regenerate-diet-plan")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> regenerateDetailedDietPlan(@PathVariable Long assessmentId) {

        // Get the assessment details
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalStateException("Assessment not found"));

        return agentExecutionService.regenerateDetailedDietPlan(assessmentId)
                .thenApply(updatedDietPlan -> {
                    // Return map response with dietPlan and assessment
                    Map<String, Object> response = new HashMap<>();
                    response.put("dietPlan", updatedDietPlan);
                    response.put("assessment", assessment);
                    return ResponseEntity.ok(response);
                });
    }

    @PostMapping("/updateDietPlan/{dietPlanId}")
    public ResponseEntity<DietPlan> updateDietPlan(
            @PathVariable Long dietPlanId,
            @RequestBody DietPlan dietPlanDetails) {
        DietPlan updatedDietPlan = assessmentService.updateDietPlan(
                dietPlanId,
                dietPlanDetails.getBreakfast(),
                dietPlanDetails.getLunch(),
                dietPlanDetails.getDinner(),
                dietPlanDetails.getAvoidFoods());
        return ResponseEntity.ok(updatedDietPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
