package com.PrakartiAyurVeda.user.service;

import com.PrakartiAyurVeda.user.dto.UserDto;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUserProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return toUserDto(user);
    }

    private UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .location(user.getLocation())
                .foodPreference(user.getFoodPreference())
                .authProvider(user.getAuthProvider())
                .build();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User update(Long id, User user) {
        User existingUser = getById(id);

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getAge() != null) {
            existingUser.setAge(user.getAge());
        }
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        if (user.getLocation() != null) {
            existingUser.setLocation(user.getLocation());
        }
        if (user.getFoodPreference() != null) {
            existingUser.setFoodPreference(user.getFoodPreference());
        }
        // Note: Password update should be handled separately with proper encryption

        return userRepository.save(existingUser);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
