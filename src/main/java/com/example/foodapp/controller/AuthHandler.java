package com.example.foodapp.controller;

import com.example.foodapp.dto.RegisterRequest;
import com.example.foodapp.dto.LoginRequest;
import com.example.foodapp.dto.AuthResponse;
import com.example.foodapp.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class AuthHandler implements HttpHandler {

    private final UserService svc = new UserService();

    // Jackson mapper that knows how to handle Java 8 dates
    private final ObjectMapper json = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if ("/auth/register".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleRegister(exchange);
        } else if ("/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleLogin(exchange);
        } else if ("/auth/check-phone".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleCheckPhone(exchange);
        } else {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        // parse the incoming JSON body
        RegisterRequest req = json.readValue(exchange.getRequestBody(), RegisterRequest.class);

        // perform registration logic and get back token + user info
        AuthResponse resp = svc.register(req);

        // serialize response to JSON
        byte[] body = json.writeValueAsBytes(resp);

        // send HTTP 201 Created
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(201, body.length);
        exchange.getResponseBody().write(body);
        exchange.close();
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        // parse the incoming JSON body
        LoginRequest req = json.readValue(exchange.getRequestBody(), LoginRequest.class);

        // perform login logic and get back token + user info
        AuthResponse resp = svc.login(req);

        // serialize response to JSON
        byte[] body = json.writeValueAsBytes(resp);

        // send HTTP 200 OK
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        exchange.getResponseBody().write(body);
        exchange.close();
    }

    private void handleCheckPhone(HttpExchange exchange) throws IOException {
        // Parse the request body to get the phone number
        Map<String, String> req = json.readValue(exchange.getRequestBody(), Map.class);
        String phone = req.get("phone");

        if (phone == null || phone.trim().isEmpty()) {
            sendError(exchange, 400, "Phone number is required");
            return;
        }

        // Check if phone exists
        boolean exists = svc.existsByPhone(phone);

        // Create response
        ObjectNode response = json.createObjectNode();
        response.put("exists", exists);
        response.put("message", exists ? "Phone number is already registered" : "Phone number is available");

        // Send response
        byte[] body = json.writeValueAsBytes(response);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
        exchange.close();
    }

    private void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        ObjectNode error = json.createObjectNode();
        error.put("error", message);
        byte[] body = json.writeValueAsBytes(error);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
        exchange.close();
    }
}
