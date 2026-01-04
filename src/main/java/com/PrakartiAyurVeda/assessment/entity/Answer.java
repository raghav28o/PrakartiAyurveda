package com.PrakartiAyurVeda.assessment.entity;

import com.PrakartiAyurVeda.common.enums.DoshaType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "answers")
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JsonBackReference
    private Assessment assessment;

    @Column(nullable = false)
    private String questionCode;

    @Column(nullable = false)
    private String answerValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoshaType doshaType;

    @Column(nullable = false)
    private int weight;
}
