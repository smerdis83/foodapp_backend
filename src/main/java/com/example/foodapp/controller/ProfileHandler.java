package com.example.foodapp.controller;

import com.example.foodapp.dto.UserResponse;
import com.example.foodapp.service.UserService;
import com.example.foodapp.security.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Handler for GET /auth/profile
 */
public class ProfileHandler implements HttpHandler {

    private final UserService svc = new UserService();

    // Jackson mapper that can handle Java 8 dates
    private final ObjectMapper json = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path   = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        // only accept GET /auth/profile
        if (!"/auth/profile".equals(path) || !"GET".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        // get Authorization header
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // missing or invalid header
            exchange.sendResponseHeaders(401, -1);
            exchange.close();
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        int userId;
        try {
            // validate token and extract user ID
            userId = JwtUtil.validateTokenAndGetUserId(token);
        } catch (Exception e) {
            // token invalid or expired
            exchange.sendResponseHeaders(401, -1);
            exchange.close();
            return;
        }

        // fetch user info
        Optional<UserResponse> maybeUser = svc.getById(userId);
        if (maybeUser.isEmpty()) {
            // user not found
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        // serialize and send user info
        byte[] body = json.writeValueAsBytes(maybeUser.get());
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
        exchange.close();
    }
}
