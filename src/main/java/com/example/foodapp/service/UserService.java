package com.example.foodapp.service;

import com.example.foodapp.dto.RegisterRequest;
import com.example.foodapp.dto.UserResponse;
import com.example.foodapp.dto.AuthResponse;
import com.example.foodapp.dto.LoginRequest;
import com.example.foodapp.security.JwtUtil;
import com.example.foodapp.model.User;
import com.example.foodapp.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Business logic around user operations.
 */
public class UserService {
    private final UserRepository repo = new UserRepository();

    /**
     * Register a new user.
     */
    public AuthResponse register(RegisterRequest req) {
        // ۱. بررسی یکتا بودن
        if (repo.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (repo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }
        // ۲. ذخیرهٔ کاربر
        User u = new User(req.getUsername(), req.getPassword(), req.getEmail());
        User saved = repo.save(u);
        // ۳. تولید توکن
        String token = JwtUtil.generateToken(saved.getId());
        // ۴. برگرداندن پاسخ
        return new AuthResponse(
                token,
                saved.getId(),
                saved.getUsername(),
                saved.getEmail()
        );
    }

    public AuthResponse login(LoginRequest req) {
        // در repo متدی بنویس existsByUsernameAndPassword یا fetchByUsername و سپس چک پسورد
        User u = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Bad credentials"));
        if (!u.getPasswordHash().equals(req.getPassword())) {
            throw new IllegalArgumentException("Bad credentials");
        }
        String token = JwtUtil.generateToken(u.getId());
        return new AuthResponse(token, u.getId(), u.getUsername(), u.getEmail());
    }

    /**
     * Get a user by id.
     */
    public Optional<UserResponse> getById(int id) {
        return repo.findById(id)
                .map(this::mapToResponse);
    }

    /**
     * Convert User entity to DTO.
     */
    private UserResponse mapToResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getCreatedAt() != null ? u.getCreatedAt() : LocalDateTime.now()
        );
    }
}
