package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import javax.sql.DataSource;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public List<Map<String, Object>> listarPacientes() {
        List<Map<String, Object>> pacientes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pacientes")) {
            while (rs.next()) {
                Map<String, Object> paciente = new HashMap<>();
                paciente.put("id_paciente", rs.getInt("id_paciente"));
                paciente.put("nombre", rs.getString("nombre"));
                paciente.put("apellido", rs.getString("apellido"));
                paciente.put("email", rs.getString("email"));
                paciente.put("telefono", rs.getString("telefono"));
                paciente.put("fecha_nacimiento", rs.getDate("fecha_nacimiento"));
                paciente.put("fecha_registro", rs.getTimestamp("fecha_registro"));
                pacientes.add(paciente);
            }
        } catch (Exception e) {
            // Manejo de error
        }
        return pacientes;
    }

    @GetMapping("/{id}")
    public Map<String, Object> obtenerPaciente(@PathVariable int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM pacientes WHERE id_paciente = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> paciente = new HashMap<>();
                paciente.put("id_paciente", rs.getInt("id_paciente"));
                paciente.put("nombre", rs.getString("nombre"));
                paciente.put("apellido", rs.getString("apellido"));
                paciente.put("email", rs.getString("email"));
                paciente.put("telefono", rs.getString("telefono"));
                paciente.put("fecha_nacimiento", rs.getDate("fecha_nacimiento"));
                paciente.put("fecha_registro", rs.getTimestamp("fecha_registro"));
                return paciente;
            }
        } catch (Exception e) {}
        return null;
    }

    @PostMapping
    public ResponseEntity<String> crearPaciente(@RequestBody Map<String, Object> body) {
        String contrasena = (String) body.get("contrasena");
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El campo 'contrasena' es obligatorio");
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO pacientes (nombre, apellido, email, telefono, fecha_nacimiento, contrasena) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, (String) body.get("nombre"));
            ps.setString(2, (String) body.get("apellido"));
            ps.setString(3, (String) body.get("email"));
            ps.setString(4, (String) body.get("telefono"));
            ps.setDate(5, body.get("fecha_nacimiento") != null ? Date.valueOf((String) body.get("fecha_nacimiento")) : null);
            ps.setString(6, contrasena);
            ps.executeUpdate();
            return ResponseEntity.ok("Paciente creado");
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("duplicate key value")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El email ya está registrado");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public String actualizarPaciente(@PathVariable int id, @RequestBody Map<String, Object> body) {
        StringBuilder sql = new StringBuilder("UPDATE pacientes SET ");
        List<Object> params = new ArrayList<>();
        List<String> sets = new ArrayList<>();

        if (body.containsKey("nombre") && body.get("nombre") != null) {
            sets.add("nombre=?");
            params.add(body.get("nombre"));
        }
        if (body.containsKey("apellido") && body.get("apellido") != null) {
            sets.add("apellido=?");
            params.add(body.get("apellido"));
        }
        if (body.containsKey("email") && body.get("email") != null) {
            sets.add("email=?");
            params.add(body.get("email"));
        }
        if (body.containsKey("telefono") && body.get("telefono") != null) {
            sets.add("telefono=?");
            params.add(body.get("telefono"));
        }
        if (body.containsKey("fecha_nacimiento") && body.get("fecha_nacimiento") != null) {
            sets.add("fecha_nacimiento=?");
            params.add(Date.valueOf((String) body.get("fecha_nacimiento")));
        }
        if (body.containsKey("contrasena") && body.get("contrasena") != null) {
            sets.add("contrasena=?");
            params.add(body.get("contrasena"));
        }

        if (sets.isEmpty()) {
            return "No hay campos para actualizar";
        }

        sql.append(String.join(", ", sets));
        sql.append(" WHERE id_paciente=?");
        params.add(id);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
            return "Paciente actualizado";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    public String eliminarPaciente(@PathVariable int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM pacientes WHERE id_paciente=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return "Paciente eliminado";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/buscar/email")
    public Map<String, Object> buscarPorEmail(@RequestParam String email) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM pacientes WHERE email=?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> paciente = new HashMap<>();
                paciente.put("id_paciente", rs.getInt("id_paciente"));
                paciente.put("nombre", rs.getString("nombre"));
                paciente.put("apellido", rs.getString("apellido"));
                paciente.put("email", rs.getString("email"));
                paciente.put("telefono", rs.getString("telefono"));
                paciente.put("fecha_nacimiento", rs.getDate("fecha_nacimiento"));
                paciente.put("fecha_registro", rs.getTimestamp("fecha_registro"));
                return paciente;
            }
        } catch (Exception e) {}
        return null;
    }
}
