package com.example.foodapp.dto;

/**
 * Request sent by client to log in a user.
 */
public class LoginRequest {
    private String username;  // user’s username
    private String password;  // user’s password

    /** No-arg constructor needed by Jackson */
    public LoginRequest() { }

    /**
     * Full constructor
     * @param username the user’s username
     * @param password the user’s password
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters and setters

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
