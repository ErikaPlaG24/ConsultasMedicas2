package com.example.backend.controller;

import com.example.backend.entity.Consulta;
import com.example.backend.repository.ConsultaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @GetMapping
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> obtenerConsulta(@PathVariable Long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);
        return consulta.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Consulta> obtenerConsultasPorPaciente(@PathVariable Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId);
    }

    @GetMapping("/medico/{medicoId}")
    public List<Consulta> obtenerConsultasPorMedico(@PathVariable Long medicoId) {
        return consultaRepository.findByMedicoId(medicoId);
    }

    @GetMapping("/estado/{estado}")
    public List<Consulta> obtenerConsultasPorEstado(@PathVariable String estado) {
        return consultaRepository.findByEstado(estado);
    }

    @PostMapping
    public ResponseEntity<Consulta> crearConsulta(@RequestBody Consulta consulta) {
        try {
            Consulta nuevaConsulta = consultaRepository.save(consulta);
            return ResponseEntity.ok(nuevaConsulta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consulta> actualizarConsulta(@PathVariable Long id, @RequestBody Consulta consultaActualizada) {
        Optional<Consulta> consultaExistente = consultaRepository.findById(id);
        if (consultaExistente.isPresent()) {
            Consulta consulta = consultaExistente.get();
            consulta.setFechaHora(consultaActualizada.getFechaHora());
            consulta.setMotivo(consultaActualizada.getMotivo());
            consulta.setEstado(consultaActualizada.getEstado());
            consulta.setNotasMedicas(consultaActualizada.getNotasMedicas());
            if (consultaActualizada.getPaciente() != null) {
                consulta.setPaciente(consultaActualizada.getPaciente());
            }
            if (consultaActualizada.getMedico() != null) {
                consulta.setMedico(consultaActualizada.getMedico());
            }
            
            Consulta consultaGuardada = consultaRepository.save(consulta);
            return ResponseEntity.ok(consultaGuardada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarConsulta(@PathVariable Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
