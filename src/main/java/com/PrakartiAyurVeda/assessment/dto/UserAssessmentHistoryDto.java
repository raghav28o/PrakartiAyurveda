package com.PrakartiAyurVeda.assessment.dto;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.diet.entity.DietPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAssessmentHistoryDto {
    private Assessment assessment;
    private DietPlan dietPlan;
}
