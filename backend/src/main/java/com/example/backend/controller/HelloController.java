package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;

@RestController
@RequestMapping("/api")
public class HelloController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/hello")
    public String hello() {
        return "Hola desde el backend 😎😎";
    }
    
    @GetMapping("/health/db")
    public String checkDatabase() {
        try {
            Connection connection = dataSource.getConnection();
            String dbName = connection.getCatalog();
            connection.close();
            return "✅ Conexión exitosa a la base de datos: " + dbName;
        } catch (Exception e) {
            return "❌ Error de conexión: " + e.getMessage();
        }
    }
}
