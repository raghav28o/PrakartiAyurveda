package com.PrakartiAyurVeda.assessment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PrakartiAyurVeda.assessment.entity.Assessment;
import com.PrakartiAyurVeda.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    List<Assessment> findByUser(User user);

    List<Assessment> findByUserId(Long userId);

    List<Assessment> findByUserIdOrderByCreatedAtDesc(Long userId);

}
