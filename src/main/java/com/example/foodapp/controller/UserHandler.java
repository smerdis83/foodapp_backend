package com.example.foodapp.controller;

import com.example.foodapp.dto.CreateUserRequest;
import com.example.foodapp.dto.UserResponse;
import com.example.foodapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class UserHandler implements HttpHandler {
    private final UserService userService = new UserService();
    private final ObjectMapper json = new ObjectMapper()
            // teach Jackson how to handle java.time types:
            .registerModule(new JavaTimeModule())
            // emit ISO-8601 strings instead of timestamps:
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void handle(HttpExchange exchange)  {
        try {
            // log incoming request
            System.out.println("Received " + exchange.getRequestMethod() +
                    " " + exchange.getRequestURI());

            String method = exchange.getRequestMethod();
            URI uri = exchange.getRequestURI();
            String path = uri.getPath();

            if ("POST".equalsIgnoreCase(method) && "/users".equals(path)) {
                handleCreate(exchange);
            } else if ("GET".equalsIgnoreCase(method) && path.startsWith("/users/")) {
                handleGetById(exchange, path);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }

        } catch (Exception e) {
            // print the full stack trace to your console
            e.printStackTrace();

            // attempt to return a 500 to client
            try {
                exchange.sendResponseHeaders(500, -1);
            } catch (IOException ignored) { }
        } finally {
            exchange.close();
        }
    }

    private void handleCreate(HttpExchange exchange) throws IOException {
        try (InputStream in = exchange.getRequestBody()) {
            CreateUserRequest req = json.readValue(in, CreateUserRequest.class);
            UserResponse created = userService.register(req);

            byte[] resp = json.writeValueAsBytes(created);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(201, resp.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resp);
            }
        }
    }

    private void handleGetById(HttpExchange exchange, String path) throws IOException {
        String idStr = path.substring("/users/".length());
        int id = Integer.parseInt(idStr);  // let NumberFormatException bubble up

        Optional<UserResponse> maybe = userService.getById(id);
        if (maybe.isPresent()) {
            byte[] resp = json.writeValueAsBytes(maybe.get());
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, resp.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(resp);
            }
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }
}
