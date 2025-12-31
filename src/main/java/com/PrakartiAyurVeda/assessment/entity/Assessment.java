package com.PrakartiAyurVeda.assessment.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.PrakartiAyurVeda.common.enums.DoshaType;
import com.PrakartiAyurVeda.user.entity.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

@Entity
@Table(name = "assessments")
@Data
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoshaType dominantDosha;

    @Column(nullable = false)
    private int vataScore;

    @Column(nullable = false)
    private int pittaScore;

    @Column(nullable = false)
    private int kaphaScore;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(
            mappedBy = "assessment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<Answer> answers;

}

