package com.example.foodapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String address;

    @Column(name = "profile_image_base64", columnDefinition = "TEXT")
    private String profileImageBase64;

    @Embedded
    private BankInfoEmbeddable bankInfo;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /** JPA requires a public or protected no-arg constructor */
    public User() { }

    /**
     * Convenience constructor for creating a new user.
     * We set createdAt to now.
     */
    public User(String fullName, String phone, String email, 
                String passwordHash, String role, String address,
                String profileImageBase64, BankInfoEmbeddable bankInfo) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.address = address;
        this.profileImageBase64 = profileImageBase64;
        this.bankInfo = bankInfo;
        this.createdAt = LocalDateTime.now();
    }

    // --- getters & setters ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfileImageBase64() { return profileImageBase64; }
    public void setProfileImageBase64(String profileImageBase64) { this.profileImageBase64 = profileImageBase64; }

    public BankInfoEmbeddable getBankInfo() { return bankInfo; }
    public void setBankInfo(BankInfoEmbeddable bankInfo) { this.bankInfo = bankInfo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
