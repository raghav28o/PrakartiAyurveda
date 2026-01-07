package com.PrakartiAyurVeda.assessment.service;

import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.repository.DietPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.assessment.repository.AssessmentRepository;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.user.entity.User;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final DietPlanRepository dietPlanRepository;

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

    public Assessment getById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found with id: " + id));
    }

    public List<Assessment> getByUserId(Long userId) {
        return assessmentRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!assessmentRepository.existsById(id)) {
            throw new RuntimeException("Assessment not found with id: " + id);
        }
        assessmentRepository.deleteById(id);
    }

    @Transactional
    public DietPlan updateDietPlan(Long dietPlanId, String breakfast, String lunch, String dinner, String avoidFoods) {
        DietPlan dietPlan = dietPlanRepository.findById(dietPlanId)
                .orElseThrow(() -> new RuntimeException("Diet plan not found with id: " + dietPlanId));

        dietPlan.setBreakfast(breakfast);
        dietPlan.setLunch(lunch);
        dietPlan.setDinner(dinner);
        dietPlan.setAvoidFoods(avoidFoods);

        return dietPlanRepository.save(dietPlan);
    }
}
