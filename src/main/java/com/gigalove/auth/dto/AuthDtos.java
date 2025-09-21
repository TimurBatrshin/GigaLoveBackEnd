package com.gigalove.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthDtos {
    @Data
    public static class RegisterRequest {
        @Email
        private String email;
        private String phone;
        @NotBlank
        private String password;
        @NotBlank
        private String name;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String phone;
        @NotBlank
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String userId;

        public AuthResponse(String token, String userId) {
            this.token = token;
            this.userId = userId;
        }
    }
}


