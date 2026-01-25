package com.PrakartiAyurVeda.user.repository;

import com.PrakartiAyurVeda.user.entity.AuthProvider;
import com.PrakartiAyurVeda.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);
}

