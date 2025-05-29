package com.example.foodapp.dto;

/**
 * Request sent by client to log in a user.
 */
public class LoginRequest {
    private String phone;
    private String password;

    /** No-arg constructor needed by Jackson */
    public LoginRequest() { }

    /**
     * Full constructor
     * @param phone the user’s phone
     * @param password the user’s password
     */
    public LoginRequest(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    // getters and setters

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
