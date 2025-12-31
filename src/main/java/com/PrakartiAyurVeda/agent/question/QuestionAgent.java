package com.PrakartiAyurVeda.agent.question;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.PrakartiAyurVeda.agent.Agent;
import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;

@Component
@Order(1)
public class QuestionAgent implements Agent {

    @Override
    public void execute(AgentContext context) {

        List<Answer> answers = context.getAnswers();

        if (answers == null || answers.isEmpty()) {
            context.setSafe(false);
            throw new IllegalStateException("No answers provided for assessment");
        }

        // Initialize assessment object (scores calculated later)
        Assessment assessment = new Assessment();
        assessment.setUser(context.getUser());

        // Link answers to assessment
        for (Answer answer : answers) {
            answer.setAssessment(assessment);
        }

        assessment.setAnswers(answers);
        context.setAssessment(assessment);
    }
}
