package com.example.foodapp.dto;

import java.time.LocalDateTime;

/**
 * What we return when we send user data back.
 */
public class UserResponse {
    private int id;
    private String username;
    private String email;
    private LocalDateTime createdAt;

    public UserResponse() { }

    public UserResponse(int id, String username, String email, LocalDateTime createdAt) {
        this.id        = id;
        this.username  = username;
        this.email     = email;
        this.createdAt = createdAt;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
