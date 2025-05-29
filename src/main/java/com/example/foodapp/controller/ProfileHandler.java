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

public class ProfileHandler implements HttpHandler {

    private final UserService svc = new UserService();

    private final ObjectMapper json = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path   = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (!"/auth/profile".equals(path) || !"GET".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.sendResponseHeaders(401, -1);
            exchange.close();
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        int userId;
        try {
            userId = JwtUtil.validateTokenAndGetUserId(token);
        } catch (Exception e) {
            exchange.sendResponseHeaders(401, -1);
            exchange.close();
            return;
        }

        Optional<UserResponse> maybeUser = svc.getById(userId);

        // <-- Use isPresent() instead of isEmpty()
        if (!maybeUser.isPresent()) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        byte[] body = json.writeValueAsBytes(maybeUser.get());
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
        exchange.close();
    }
}
