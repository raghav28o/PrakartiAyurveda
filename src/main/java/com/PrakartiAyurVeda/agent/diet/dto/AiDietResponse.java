package com.PrakartiAyurVeda.agent.diet.dto;

import com.PrakartiAyurVeda.diet.dto.DailyDietDto;
import com.PrakartiAyurVeda.diet.dto.DietPlanDto;
import com.PrakartiAyurVeda.common.enums.DoshaType;

import java.util.List;
import java.util.stream.Collectors;

public record AiDietResponse(
    List<AiDailyDiet> weeklyDiet,
    String avoidFoods
) {
    public DietPlanDto toDietPlanDto(DoshaType dosha) {
        DietPlanDto dietPlanDto = new DietPlanDto();
        dietPlanDto.setDoshaType(dosha);
        dietPlanDto.setAvoidFoods(avoidFoods);

        if (weeklyDiet != null && !weeklyDiet.isEmpty()) {
            List<DailyDietDto> dailyDiets = weeklyDiet.stream()
                .map(AiDailyDiet::toDailyDietDto)
                .collect(Collectors.toList());
            dietPlanDto.setDailyDiets(dailyDiets);

            // Set top-level meal fields from the first day for compatibility
            DailyDietDto firstDay = dailyDiets.get(0);
            dietPlanDto.setBreakfast(firstDay.getBreakfast());
            dietPlanDto.setLunch(firstDay.getLunch());
            dietPlanDto.setDinner(firstDay.getDinner());
        }

        return dietPlanDto;
    }
}
