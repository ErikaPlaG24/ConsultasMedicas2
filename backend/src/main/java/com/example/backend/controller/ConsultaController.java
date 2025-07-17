package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public List<Map<String, Object>> listarConsultas() {
        List<Map<String, Object>> consultas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM consultas")) {
            while (rs.next()) {
                Map<String, Object> consulta = new HashMap<>();
                consulta.put("id_consulta", rs.getInt("id_consulta"));
                consulta.put("id_paciente", rs.getInt("id_paciente"));
                consulta.put("id_medico", rs.getInt("id_medico"));
                consulta.put("fecha_hora", rs.getTimestamp("fecha_hora"));
                consulta.put("motivo", rs.getString("motivo"));
                consulta.put("estado", rs.getString("estado"));
                consulta.put("notas_medicas", rs.getString("notas_medicas"));
                consulta.put("fecha_creacion", rs.getTimestamp("fecha_creacion"));
                consultas.add(consulta);
            }
        } catch (Exception e) {}
        return consultas;
    }

    @GetMapping("/{id}")
    public Map<String, Object> obtenerConsulta(@PathVariable int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM consultas WHERE id_consulta = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> consulta = new HashMap<>();
                consulta.put("id_consulta", rs.getInt("id_consulta"));
                consulta.put("id_paciente", rs.getInt("id_paciente"));
                consulta.put("id_medico", rs.getInt("id_medico"));
                consulta.put("fecha_hora", rs.getTimestamp("fecha_hora"));
                consulta.put("motivo", rs.getString("motivo"));
                consulta.put("estado", rs.getString("estado"));
                consulta.put("notas_medicas", rs.getString("notas_medicas"));
                consulta.put("fecha_creacion", rs.getTimestamp("fecha_creacion"));
                return consulta;
            }
        } catch (Exception e) {}
        return null;
    }

    @PostMapping
    public ResponseEntity<String> crearConsulta(@RequestBody Map<String, Object> body) {
        Integer idPaciente = (Integer) body.get("id_paciente");
        Integer idMedico = (Integer) body.get("id_medico");
        String fechaHoraStr = (String) body.get("fecha_hora");

        try (Connection conn = dataSource.getConnection()) {
            // Verificar existencia de paciente
            try (PreparedStatement psPaciente = conn.prepareStatement("SELECT COUNT(*) FROM pacientes WHERE id_paciente = ?")) {
                psPaciente.setInt(1, idPaciente);
                ResultSet rsPaciente = psPaciente.executeQuery();
                if (!rsPaciente.next() || rsPaciente.getInt(1) == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El paciente no existe");
                }
            }
            // Verificar existencia de medico
            try (PreparedStatement psMedico = conn.prepareStatement("SELECT COUNT(*) FROM medicos WHERE id_medico = ?")) {
                psMedico.setInt(1, idMedico);
                ResultSet rsMedico = psMedico.executeQuery();
                if (!rsMedico.next() || rsMedico.getInt(1) == 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El médico no existe");
                }
            }
            // Verificar formato de fecha_hora
            Timestamp fechaHora;
            try {
                fechaHora = Timestamp.valueOf(fechaHoraStr);
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
            }
            // Insertar consulta
            try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO consultas (id_paciente, id_medico, fecha_hora, motivo, estado, notas_medicas) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, idPaciente);
                ps.setInt(2, idMedico);
                ps.setTimestamp(3, fechaHora);
                ps.setString(4, (String) body.get("motivo"));
                ps.setString(5, (String) body.get("estado"));
                ps.setString(6, (String) body.get("notas_medicas"));
                ps.executeUpdate();
                return ResponseEntity.ok("Consulta creada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarConsulta(@PathVariable int id, @RequestBody Map<String, Object> body) {
        StringBuilder sql = new StringBuilder("UPDATE consultas SET ");
        List<Object> params = new ArrayList<>();
        List<String> sets = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            // Verificar existencia de paciente si se actualiza
            if (body.containsKey("id_paciente") && body.get("id_paciente") != null) {
                Integer idPaciente = (Integer) body.get("id_paciente");
                try (PreparedStatement psPaciente = conn.prepareStatement("SELECT COUNT(*) FROM pacientes WHERE id_paciente = ?")) {
                    psPaciente.setInt(1, idPaciente);
                    ResultSet rsPaciente = psPaciente.executeQuery();
                    if (!rsPaciente.next() || rsPaciente.getInt(1) == 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El paciente no existe");
                    }
                }
                sets.add("id_paciente=?");
                params.add(idPaciente);
            }
            // Verificar existencia de medico si se actualiza
            if (body.containsKey("id_medico") && body.get("id_medico") != null) {
                Integer idMedico = (Integer) body.get("id_medico");
                try (PreparedStatement psMedico = conn.prepareStatement("SELECT COUNT(*) FROM medicos WHERE id_medico = ?")) {
                    psMedico.setInt(1, idMedico);
                    ResultSet rsMedico = psMedico.executeQuery();
                    if (!rsMedico.next() || rsMedico.getInt(1) == 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El médico no existe");
                    }
                }
                sets.add("id_medico=?");
                params.add(idMedico);
            }
            // Verificar formato de fecha_hora si se actualiza
            if (body.containsKey("fecha_hora") && body.get("fecha_hora") != null) {
                String fechaHoraStr = (String) body.get("fecha_hora");
                Timestamp fechaHora;
                try {
                    fechaHora = Timestamp.valueOf(fechaHoraStr);
                } catch (Exception ex) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]");
                }
                sets.add("fecha_hora=?");
                params.add(fechaHora);
            }
            if (body.containsKey("motivo") && body.get("motivo") != null) {
                sets.add("motivo=?");
                params.add(body.get("motivo"));
            }
            if (body.containsKey("estado") && body.get("estado") != null) {
                sets.add("estado=?");
                params.add(body.get("estado"));
            }
            if (body.containsKey("notas_medicas") && body.get("notas_medicas") != null) {
                sets.add("notas_medicas=?");
                params.add(body.get("notas_medicas"));
            }

            if (sets.isEmpty()) {
                return ResponseEntity.badRequest().body("No hay campos para actualizar");
            }

            sql.append(String.join(", ", sets));
            sql.append(" WHERE id_consulta=?");
            params.add(id);

            try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                ps.executeUpdate();
                return ResponseEntity.ok("Consulta actualizada");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public String eliminarConsulta(@PathVariable int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM consultas WHERE id_consulta=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return "Consulta eliminada";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/paciente/{id_paciente}")
    public List<Map<String, Object>> listarPorPaciente(@PathVariable int id_paciente) {
        List<Map<String, Object>> consultas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM consultas WHERE id_paciente=?")) {
            ps.setInt(1, id_paciente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> consulta = new HashMap<>();
                consulta.put("id_consulta", rs.getInt("id_consulta"));
                consulta.put("id_paciente", rs.getInt("id_paciente"));
                consulta.put("id_medico", rs.getInt("id_medico"));
                consulta.put("fecha_hora", rs.getTimestamp("fecha_hora"));
                consulta.put("motivo", rs.getString("motivo"));
                consulta.put("estado", rs.getString("estado"));
                consulta.put("notas_medicas", rs.getString("notas_medicas"));
                consulta.put("fecha_creacion", rs.getTimestamp("fecha_creacion"));
                consultas.add(consulta);
            }
        } catch (Exception e) {}
        return consultas;
    }

    @GetMapping("/medico/{id_medico}")
    public List<Map<String, Object>> listarPorMedico(@PathVariable int id_medico) {
        List<Map<String, Object>> consultas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM consultas WHERE id_medico=?")) {
            ps.setInt(1, id_medico);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> consulta = new HashMap<>();
                consulta.put("id_consulta", rs.getInt("id_consulta"));
                consulta.put("id_paciente", rs.getInt("id_paciente"));
                consulta.put("id_medico", rs.getInt("id_medico"));
                consulta.put("fecha_hora", rs.getTimestamp("fecha_hora"));
                consulta.put("motivo", rs.getString("motivo"));
                consulta.put("estado", rs.getString("estado"));
                consulta.put("notas_medicas", rs.getString("notas_medicas"));
                consulta.put("fecha_creacion", rs.getTimestamp("fecha_creacion"));
                consultas.add(consulta);
            }
        } catch (Exception e) {}
        return consultas;
    }
}
