package com.PrakartiAyurVeda.agent.dosha;

import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.PrakartiAyurVeda.agent.Agent;
import com.PrakartiAyurVeda.agent.context.AgentContext;
import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.common.enums.DoshaType;

@Component
@Order(2)
public class DoshaEvaluationAgent implements Agent {

    @Override
    public void execute(AgentContext context) {

        Assessment assessment = context.getAssessment();
        List<Answer> answers = context.getAnswers();

        int vata = 0;
        int pitta = 0;
        int kapha = 0;

        for (Answer answer : answers) {
            switch (answer.getDoshaType()) {
                case VATA -> vata += answer.getWeight();
                case PITTA -> pitta += answer.getWeight();
                case KAPHA -> kapha += answer.getWeight();
            }
        }

        assessment.setVataScore(vata);
        assessment.setPittaScore(pitta);
        assessment.setKaphaScore(kapha);

        DoshaType dominantDosha = determineDominantDosha(vata, pitta, kapha);

        assessment.setDominantDosha(dominantDosha);
        context.setDominantDosha(dominantDosha);
    }

    private DoshaType determineDominantDosha(int vata, int pitta, int kapha) {

        if (vata >= pitta && vata >= kapha) {
            return DoshaType.VATA;
        }
        if (pitta >= kapha) {
            return DoshaType.PITTA;
        }
        return DoshaType.KAPHA;
    }
}
