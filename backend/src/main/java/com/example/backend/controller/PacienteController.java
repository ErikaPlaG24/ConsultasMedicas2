package com.example.backend.controller;

import com.example.backend.entity.Paciente;
import com.example.backend.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPaciente(@PathVariable Long id) {
        Optional<Paciente> paciente = pacienteRepository.findById(id);
        return paciente.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        try {
            // Validar que el email no existe
            if (pacienteRepository.existsByEmail(paciente.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            
            Paciente nuevoPaciente = pacienteRepository.save(paciente);
            return ResponseEntity.ok(nuevoPaciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente pacienteActualizado) {
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(id);
        if (pacienteExistente.isPresent()) {
            Paciente paciente = pacienteExistente.get();
            paciente.setNombre(pacienteActualizado.getNombre());
            paciente.setApellido(pacienteActualizado.getApellido());
            paciente.setEmail(pacienteActualizado.getEmail());
            paciente.setTelefono(pacienteActualizado.getTelefono());
            paciente.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
            if (pacienteActualizado.getContrasena() != null) {
                paciente.setContrasena(pacienteActualizado.getContrasena());
            }
            
            Paciente pacienteGuardado = pacienteRepository.save(paciente);
            return ResponseEntity.ok(pacienteGuardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
