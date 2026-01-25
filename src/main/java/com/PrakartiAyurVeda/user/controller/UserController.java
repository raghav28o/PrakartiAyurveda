package com.PrakartiAyurVeda.user.controller;

import com.PrakartiAyurVeda.auth.UserPrincipal;
import com.PrakartiAyurVeda.user.dto.UserDto;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getLoggedInUserProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
        UserDto userDto = userService.getUserProfileByEmail(currentUser.getUsername());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.update(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
