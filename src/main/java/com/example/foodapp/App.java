package com.example.foodapp;

import com.example.foodapp.config.HibernateUtil;
import com.example.foodapp.controller.UserHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws Exception {
        // 1) initialize Hibernate
        HibernateUtil.getSessionFactory();

        // 2) start HTTP server on port 8000
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/users", new UserHandler());
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();

        System.out.println("Server running at http://localhost:8000");
    }
}
