package com.example.foodapp.service;

import com.example.foodapp.dto.CreateUserRequest;
import com.example.foodapp.dto.UserResponse;
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
    public UserResponse register(CreateUserRequest req) {
        // in real life, hash the password!
        User user = new User(req.getUsername(), req.getPassword(), req.getEmail());
        User saved = repo.save(user);

        return mapToResponse(saved);
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
