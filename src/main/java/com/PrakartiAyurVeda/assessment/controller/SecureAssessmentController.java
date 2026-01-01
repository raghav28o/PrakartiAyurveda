package com.PrakartiAyurVeda.assessment.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.PrakartiAyurVeda.agent.orchestrator.AgentExecutionService;
import com.PrakartiAyurVeda.assessment.entity.Answer;
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

    @PostMapping("/run")
    public DietPlan runAssessment(@RequestBody List<Answer> answers) {

        // 🔐 Extract authenticated user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authenticated user not found")
                );

        // 🚀 Run secure agentic flow
        return agentExecutionService.runFullAssessment(user, answers);
    }
}
