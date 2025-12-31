package com.PrakartiAyurVeda.agent.context;

import java.util.List;

import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.user.entity.User;

import lombok.Data;

@Data
public class AgentContext {

    // User initiating the flow
    private User user;

    // Assessment in progress or completed
    private Assessment assessment;

    // Answers collected so far
    private List<Answer> answers;

    // Computed dosha
    private DoshaType dominantDosha;

    // Generated diet plan
    private DietPlan dietPlan;

    // Flow control flags
    private boolean safe = true;
    private boolean completed = false;
}
