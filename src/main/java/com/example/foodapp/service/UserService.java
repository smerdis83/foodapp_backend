package com.example.foodapp.service;

import com.example.foodapp.dto.RegisterRequest;
import com.example.foodapp.dto.LoginRequest;
import com.example.foodapp.dto.AuthResponse;
import com.example.foodapp.dto.UserResponse;
import com.example.foodapp.model.User;
import com.example.foodapp.model.BankInfoEmbeddable;
import com.example.foodapp.repository.UserRepository;
import com.example.foodapp.security.JwtUtil;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Business logic around user operations: register, login, profile lookup.
 */
public class UserService {
    private final UserRepository repo = new UserRepository();

    /**
     * Check if a phone number is already registered.
     * @param phone the phone number to check
     * @return true if the phone number exists, false otherwise
     */
    public boolean existsByPhone(String phone) {
        return repo.existsByPhone(phone);
    }

    /**
     * Register a new user.
     * @param req the registration request containing user details
     * @return AuthResponse with JWT token and user info
     * @throws IllegalArgumentException if validation fails
     */
    public AuthResponse register(RegisterRequest req) {
        // Validate unique constraints
        if (repo.existsByPhone(req.getPhone())) {
            throw new IllegalArgumentException("Phone number already taken");
        }
        if (req.getEmail() != null && repo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }

        // Convert bank info if present
        BankInfoEmbeddable bankInfo = null;
        if (req.getBankInfo() != null) {
            bankInfo = new BankInfoEmbeddable();
            bankInfo.setBankName(req.getBankInfo().getBankName());
            bankInfo.setAccountNumber(req.getBankInfo().getAccountNumber());
        }

        // Create and save user
        User user = new User(
            req.getFullName(),
            req.getPhone(),
            req.getEmail(),
            req.getPassword(), // Store password directly for now
            req.getRole(),
            req.getAddress(),
            req.getProfileImageBase64(),
            bankInfo
        );

        User saved = repo.save(user);

        // Generate JWT token
        String token = JwtUtil.generateToken(saved.getId());

        // Return response
        return new AuthResponse(
            "Registration successful",
            token,
            String.valueOf(saved.getId()),
            toUserResponse(saved)
        );
    }

    /**
     * Authenticate a user.
     * @param req the login request with phone and password
     * @return AuthResponse with JWT token and user info
     * @throws IllegalArgumentException if credentials are invalid
     */
    public AuthResponse login(LoginRequest req) {
        // Find user by phone
        User user = repo.findByPhone(req.getPhone())
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        // Verify password (storing raw password for now)
        if (!user.getPasswordHash().equals(req.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        // Generate JWT token
        String token = JwtUtil.generateToken(user.getId());

        // Return response
        return new AuthResponse(
            "Login successful",
            token,
            String.valueOf(user.getId()),
            toUserResponse(user)
        );
    }

    /**
     * Get user profile by ID.
     * @param id the user ID
     * @return Optional<UserResponse> containing user data if found
     */
    public Optional<UserResponse> getById(int id) {
        return repo.findById(id)
            .map(this::toUserResponse);
    }

    /**
     * Convert User entity to UserResponse DTO.
     */
    private UserResponse toUserResponse(User u) {
        return new UserResponse(
            u.getId(),
            u.getFullName(),
            u.getPhone(),
            u.getEmail(),
            u.getRole(),
            u.getAddress(),
            u.getProfileImageBase64(),
            u.getBankInfo() != null ? new com.example.foodapp.dto.BankInfo(
                u.getBankInfo().getBankName(),
                u.getBankInfo().getAccountNumber()
            ) : null
        );
    }
}
