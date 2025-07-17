package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public List<Map<String, Object>> listarMedicos() {
        List<Map<String, Object>> medicos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM medicos")) {
            while (rs.next()) {
                Map<String, Object> medico = new HashMap<>();
                medico.put("id_medico", rs.getInt("id_medico"));
                medico.put("nombre", rs.getString("nombre"));
                medico.put("apellido", rs.getString("apellido"));
                medico.put("email", rs.getString("email"));
                medico.put("telefono", rs.getString("telefono"));
                medico.put("especialidad", rs.getString("especialidad"));
                medico.put("registro_profesional", rs.getString("registro_profesional"));
                medicos.add(medico);
            }
        } catch (Exception e) {}
        return medicos;
    }

    @GetMapping("/{id}")
    public Map<String, Object> obtenerMedico(@PathVariable int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM medicos WHERE id_medico = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> medico = new HashMap<>();
                medico.put("id_medico", rs.getInt("id_medico"));
                medico.put("nombre", rs.getString("nombre"));
                medico.put("apellido", rs.getString("apellido"));
                medico.put("email", rs.getString("email"));
                medico.put("telefono", rs.getString("telefono"));
                medico.put("especialidad", rs.getString("especialidad"));
                medico.put("registro_profesional", rs.getString("registro_profesional"));
                return medico;
            }
        } catch (Exception e) {}
        return null;
    }

    @PostMapping
    public ResponseEntity<String> crearMedico(@RequestBody Map<String, Object> body) {
        String email = (String) body.get("email");
        String registroProfesional = (String) body.get("registro_profesional");

        try (Connection conn = dataSource.getConnection()) {
            // Verificar email
            try (PreparedStatement psEmail = conn.prepareStatement("SELECT COUNT(*) FROM medicos WHERE email = ?")) {
                psEmail.setString(1, email);
                ResultSet rsEmail = psEmail.executeQuery();
                if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El email ya está registrado");
                }
            }
            // Verificar registro_profesional
            try (PreparedStatement psReg = conn.prepareStatement("SELECT COUNT(*) FROM medicos WHERE registro_profesional = ?")) {
                psReg.setString(1, registroProfesional);
                ResultSet rsReg = psReg.executeQuery();
                if (rsReg.next() && rsReg.getInt(1) > 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El registro profesional ya está registrado");
                }
            }
            // Insertar médico si no hay duplicados
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO medicos (nombre, apellido, email, telefono, especialidad, registro_profesional, contrasena) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, (String) body.get("nombre"));
                ps.setString(2, (String) body.get("apellido"));
                ps.setString(3, email);
                ps.setString(4, (String) body.get("telefono"));
                ps.setString(5, (String) body.get("especialidad"));
                ps.setString(6, registroProfesional);
                ps.setString(7, (String) body.get("contrasena"));
                ps.executeUpdate();
                return ResponseEntity.ok("Medico creado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarMedico(@PathVariable int id, @RequestBody Map<String, Object> body) {
        StringBuilder sql = new StringBuilder("UPDATE medicos SET ");
        List<Object> params = new ArrayList<>();
        List<String> sets = new ArrayList<>();

        // Verificar duplicados antes de actualizar
        try (Connection conn = dataSource.getConnection()) {
            if (body.containsKey("email") && body.get("email") != null) {
                String email = (String) body.get("email");
                try (PreparedStatement psEmail = conn.prepareStatement("SELECT COUNT(*) FROM medicos WHERE email = ? AND id_medico <> ?")) {
                    psEmail.setString(1, email);
                    psEmail.setInt(2, id);
                    ResultSet rsEmail = psEmail.executeQuery();
                    if (rsEmail.next() && rsEmail.getInt(1) > 0) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El email ya está registrado");
                    }
                }
            }
            if (body.containsKey("registro_profesional") && body.get("registro_profesional") != null) {
                String registroProfesional = (String) body.get("registro_profesional");
                try (PreparedStatement psReg = conn.prepareStatement("SELECT COUNT(*) FROM medicos WHERE registro_profesional = ? AND id_medico <> ?")) {
                    psReg.setString(1, registroProfesional);
                    psReg.setInt(2, id);
                    ResultSet rsReg = psReg.executeQuery();
                    if (rsReg.next() && rsReg.getInt(1) > 0) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: El registro profesional ya está registrado");
                    }
                }
            }

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
            if (body.containsKey("especialidad") && body.get("especialidad") != null) {
                sets.add("especialidad=?");
                params.add(body.get("especialidad"));
            }
            if (body.containsKey("registro_profesional") && body.get("registro_profesional") != null) {
                sets.add("registro_profesional=?");
                params.add(body.get("registro_profesional"));
            }
            if (body.containsKey("contrasena") && body.get("contrasena") != null) {
                sets.add("contrasena=?");
                params.add(body.get("contrasena"));
            }

            if (sets.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay campos para actualizar");
            }

            sql.append(String.join(", ", sets));
            sql.append(" WHERE id_medico=?");
            params.add(id);

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                ps.executeUpdate();
                return ResponseEntity.ok("Medico actualizado");
            }
        } catch (Exception e) {
            // Si el error es por clave duplicada, retorna 409
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate key value")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String eliminarMedico(@PathVariable int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM medicos WHERE id_medico=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return "Medico eliminado";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/buscar/especialidad")
    public List<Map<String, Object>> buscarPorEspecialidad(@RequestParam String especialidad) {
        List<Map<String, Object>> medicos = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM medicos WHERE especialidad=?")) {
            ps.setString(1, especialidad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> medico = new HashMap<>();
                medico.put("id_medico", rs.getInt("id_medico"));
                medico.put("nombre", rs.getString("nombre"));
                medico.put("apellido", rs.getString("apellido"));
                medico.put("email", rs.getString("email"));
                medico.put("telefono", rs.getString("telefono"));
                medico.put("especialidad", rs.getString("especialidad"));
                medico.put("registro_profesional", rs.getString("registro_profesional"));
                medicos.add(medico);
            }
        } catch (Exception e) {}
        return medicos;
    }
}
