package com.PrakartiAyurVeda.diet.dto;

import com.PrakartiAyurVeda.common.enums.DoshaType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DietPlanDto {

    private Long id;
    private Long assessmentId;
    private DoshaType doshaType;
    private String breakfast;
    private String lunch;
    private String dinner;
    private String avoidFoods;
    private LocalDate createdDate;
}
