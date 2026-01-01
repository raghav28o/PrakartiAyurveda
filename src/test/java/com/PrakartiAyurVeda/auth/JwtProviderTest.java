package com.PrakartiAyurVeda.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    private String testEmail;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
    }

    @Test
    void testGenerateToken() {
        String token = jwtProvider.generateToken(testEmail);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
        System.out.println("Generated Token: " + token);
    }

    @Test
    void testValidateToken() {
        String token = jwtProvider.generateToken(testEmail);

        boolean isValid = jwtProvider.validateToken(token);

        assertTrue(isValid, "Token should be valid");
        System.out.println("Token validation: " + isValid);
    }

    @Test
    void testGetEmailFromToken() {
        String token = jwtProvider.generateToken(testEmail);

        String extractedEmail = jwtProvider.getEmailFromToken(token);

        assertEquals(testEmail, extractedEmail, "Extracted email should match original email");
        System.out.println("Extracted Email: " + extractedEmail);
    }

    @Test
    void testInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtProvider.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should return false");
    }

    @Test
    void testTokenWorkflow() {
        // Generate token
        String token = jwtProvider.generateToken(testEmail);
        assertNotNull(token);

        // Validate token
        assertTrue(jwtProvider.validateToken(token));

        // Extract email
        String email = jwtProvider.getEmailFromToken(token);
        assertEquals(testEmail, email);

        System.out.println("JWT Workflow Test Passed!");
        System.out.println("Email: " + email);
        System.out.println("Token Valid: " + jwtProvider.validateToken(token));
    }
}
