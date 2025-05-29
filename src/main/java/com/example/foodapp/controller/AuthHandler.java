package com.example.foodapp.controller;

import com.example.foodapp.dto.RegisterRequest;
import com.example.foodapp.dto.LoginRequest;
import com.example.foodapp.dto.AuthResponse;
import com.example.foodapp.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class AuthHandler implements HttpHandler {

    private final UserService svc = new UserService();

    // Jackson mapper that knows how to handle Java 8 dates
    private final ObjectMapper json = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path   = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if ("/auth/register".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleRegister(exchange);
        } else if ("/auth/login".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleLogin(exchange);
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
}
