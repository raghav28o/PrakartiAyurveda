package com.PrakartiAyurVeda.diet.controller;

import com.PrakartiAyurVeda.diet.entity.DietPlan;
import com.PrakartiAyurVeda.diet.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diets")
@RequiredArgsConstructor
public class DietPlanController {

    private final DietService dietService;

    @GetMapping("/{id}")
    public ResponseEntity<DietPlan> getDietPlanById(@PathVariable Long id) {
        DietPlan dietPlan = dietService.getById(id);
        return ResponseEntity.ok(dietPlan);
    }

    @GetMapping("/assessment/{assessmentId}")
    public ResponseEntity<DietPlan> getDietPlanByAssessmentId(@PathVariable Long assessmentId) {
        DietPlan dietPlan = dietService.getByAssessmentId(assessmentId);
        return ResponseEntity.ok(dietPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDietPlan(@PathVariable Long id) {
        dietService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
