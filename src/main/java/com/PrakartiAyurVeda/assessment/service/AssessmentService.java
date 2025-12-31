package com.PrakartiAyurVeda.assessment.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.repository.AssessmentRepository;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Transactional
    public Assessment createAssessment(User user, List<Answer> answers) {

        Assessment assessment = new Assessment();
        assessment.setUser(user);

        int vata = 0;
        int pitta = 0;
        int kapha = 0;

        for (Answer answer : answers) {
            answer.setAssessment(assessment);

            switch (answer.getDoshaType()) {
                case VATA -> vata += answer.getWeight();
                case PITTA -> pitta += answer.getWeight();
                case KAPHA -> kapha += answer.getWeight();
            }
        }

        assessment.setVataScore(vata);
        assessment.setPittaScore(pitta);
        assessment.setKaphaScore(kapha);

        assessment.setDominantDosha(determineDominantDosha(vata, pitta, kapha));
        assessment.setAnswers(answers);

        return assessmentRepository.save(assessment);
    }

    private DoshaType determineDominantDosha(int vata, int pitta, int kapha) {
        if (vata >= pitta && vata >= kapha) {
            return DoshaType.VATA;
        } else if (pitta >= kapha) {
            return DoshaType.PITTA;
        } else {
            return DoshaType.KAPHA;
        }
    }
}
