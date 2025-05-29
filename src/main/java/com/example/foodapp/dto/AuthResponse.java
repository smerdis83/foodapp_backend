package com.example.foodapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response returned after register or login operations
 */
public class AuthResponse {
    private String message;
    private String token;
    @JsonProperty("user_id")
    private String userId;
    private UserResponse user;

    /** No-arg constructor needed by Jackson */
    public AuthResponse() { }

    /**
     * Full constructor
     * @param message the message
     * @param token   the JWT token
     * @param userId  the user’s id
     * @param user    the user response
     */
    public AuthResponse(String message, String token, String userId, UserResponse user) {
        this.message = message;
        this.token = token;
        this.userId = userId;
        this.user = user;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
