package com.PrakartiAyurVeda.agent.orchestrator;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agent-test")
@RequiredArgsConstructor
public class AgentTestController {

    private final AgentExecutionService executionService;
    private final UserService userService;

    @PostMapping("/user/{userId}")
    public DietPlan runAgentFlow(
            @PathVariable Long userId,
            @RequestBody List<Answer> answers) {

        User user = userService.getById(userId);
        return executionService.runFullAssessment(user, answers);
    }
}
