package com.PrakartiAyurVeda.auth.handler;

import com.PrakartiAyurVeda.auth.JwtProvider;
import com.PrakartiAyurVeda.user.entity.AuthProvider;
import com.PrakartiAyurVeda.user.entity.User;
import com.PrakartiAyurVeda.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> userOptional = userRepository.findByEmailAndAuthProvider(email, AuthProvider.GOOGLE);
        User user;
        if (userOptional.isEmpty()) {
            user = User.builder()
                    .email(email)
                    .name(name)
                    .authProvider(AuthProvider.GOOGLE)
                    .build();
            userRepository.save(user);
        } else {
            user = userOptional.get();
        }
        System.out.println("user: " + user.getEmail() + ", " + user.getAuthProvider());
        System.out.println("userId: " + user.getId());

        String token = jwtProvider.generateToken(user.getEmail());
        System.out.println("Generated JWT Token: " + token);

        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
