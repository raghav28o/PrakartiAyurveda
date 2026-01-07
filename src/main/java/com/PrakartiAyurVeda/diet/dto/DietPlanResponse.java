package com.PrakartiAyurVeda.diet.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.PrakartiAyurVeda.assessment.entity.Answer;
import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DietPlanResponse {

    // DietPlan fields
    private Long id;
    private Long assessmentId;
    private DoshaType doshaType;
    private String breakfast;
    private String lunch;
    private String dinner;
    private String avoidFoods;
    private LocalDate createdDate;

    // Assessment fields
    private Long assessmentIdData;
    private DoshaType dominantDosha;
    private int vataScore;
    private int pittaScore;
    private int kaphaScore;
    private LocalDateTime assessmentCreatedAt;
    private List<Answer> answers;

    // User fields (from Assessment)
    private Long userId;
    private String userName;
    private String userEmail;
    private Integer userAge;
    private String userGender;
    private String userLocation;

    /**
     * Factory method to create response from DietPlan and Assessment
     */
    public static DietPlanResponse from(DietPlan dietPlan, com.PrakartiAyurVeda.assessment.entity.Assessment assessment, User user) {
        return DietPlanResponse.builder()
                // DietPlan
                .id(dietPlan.getId())
                .assessmentId(dietPlan.getAssessmentId())
                .doshaType(dietPlan.getDoshaType())
                .breakfast(dietPlan.getBreakfast())
                .lunch(dietPlan.getLunch())
                .dinner(dietPlan.getDinner())
                .avoidFoods(dietPlan.getAvoidFoods())
                .createdDate(dietPlan.getCreatedDate())
                // Assessment
                .assessmentIdData(assessment.getId())
                .dominantDosha(assessment.getDominantDosha())
                .vataScore(assessment.getVataScore())
                .pittaScore(assessment.getPittaScore())
                .kaphaScore(assessment.getKaphaScore())
                .assessmentCreatedAt(assessment.getCreatedAt())
                .answers(assessment.getAnswers())
                // User
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .userAge(user.getAge())
                .userGender(user.getGender())
                .userLocation(user.getLocation())
                .build();
    }
}
