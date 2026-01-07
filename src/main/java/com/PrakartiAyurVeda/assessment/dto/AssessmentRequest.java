package com.PrakartiAyurVeda.assessment.dto;

import java.util.List;

import com.PrakartiAyurVeda.assessment.entity.Answer;

import lombok.Data;

@Data
public class AssessmentRequest {
    private Long userId;
    private List<Answer> answers;
}
