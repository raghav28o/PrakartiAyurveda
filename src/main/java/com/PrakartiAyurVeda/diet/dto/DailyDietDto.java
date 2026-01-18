package com.PrakartiAyurVeda.diet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.DayOfWeek;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyDietDto {
    private DayOfWeek dayOfWeek;
    private String breakfast;
    private String lunch;
    private String dinner;
}
