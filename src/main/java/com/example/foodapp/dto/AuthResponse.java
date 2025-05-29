package com.example.foodapp.dto;

/**
 * Response returned after successful register or login.
 * Contains the JWT token and basic user info.
 */
public class AuthResponse {
    private String token;     // JWT to authenticate future requests
    private int id;           // user’s unique identifier
    private String username;  // user’s username
    private String email;     // user’s email address

    /** No-arg constructor needed by Jackson */
    public AuthResponse() { }

    /**
     * Full constructor
     * @param token    the JWT token
     * @param id       the user’s id
     * @param username the user’s username
     * @param email    the user’s email
     */
    public AuthResponse(String token, int id, String username, String email) {
        this.token    = token;
        this.id       = id;
        this.username = username;
        this.email    = email;
    }

    // getters and setters

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
