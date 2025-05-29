package com.example.foodapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * What we return when we send user data back.
 */
public class UserResponse {
    private int id;
    
    @JsonProperty("full_name")
    private String fullName;
    
    private String phone;
    private String email;
    private String role;
    private String address;
    
    @JsonProperty("profile_image_base64")
    private String profileImageBase64;
    
    @JsonProperty("bank_info")
    private BankInfo bankInfo;

    public UserResponse() {}

    public UserResponse(int id, String fullName, String phone, String email,
                       String role, String address, String profileImageBase64,
                       BankInfo bankInfo) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.profileImageBase64 = profileImageBase64;
        this.bankInfo = bankInfo;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImageBase64() {
        return profileImageBase64;
    }

    public void setProfileImageBase64(String profileImageBase64) {
        this.profileImageBase64 = profileImageBase64;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }
}
