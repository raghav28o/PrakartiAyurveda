package com.PrakartiAyurVeda.user.dto;

import com.PrakartiAyurVeda.user.entity.AuthProvider;
import com.PrakartiAyurVeda.user.entity.FoodPreference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String gender;
    private String location;
    private FoodPreference foodPreference;
    private AuthProvider authProvider;
}
