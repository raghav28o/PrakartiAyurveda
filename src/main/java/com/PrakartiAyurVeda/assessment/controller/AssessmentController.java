package com.PrakartiAyurVeda.assessment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.service.AssessmentService;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final UserService userService;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        assessmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
