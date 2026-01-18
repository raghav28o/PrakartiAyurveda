package com.PrakartiAyurVeda.agent.diet.dto;

import com.PrakartiAyurVeda.diet.dto.DailyDietDto;

import java.time.DayOfWeek;

public record AiDailyDiet(
    String dayOfWeek,
    String breakfast,
    String lunch,
    String dinner
) {
    public DailyDietDto toDailyDietDto() {
        return new DailyDietDto(
            DayOfWeek.valueOf(dayOfWeek.toUpperCase()),
            breakfast,
            lunch,
            dinner
        );
    }
}
