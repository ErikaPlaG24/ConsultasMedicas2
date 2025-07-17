package com.example.backend.controller;

import com.example.backend.entity.Medico;
import com.example.backend.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtenerMedico(@PathVariable Long id) {
        Optional<Medico> medico = medicoRepository.findById(id);
        return medico.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especialidad/{especialidad}")
    public List<Medico> obtenerMedicosPorEspecialidad(@PathVariable String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    @PostMapping
    public ResponseEntity<Medico> crearMedico(@RequestBody Medico medico) {
        try {
            // Validar que el email no existe
            if (medicoRepository.existsByEmail(medico.getEmail())) {
                return ResponseEntity.badRequest().build();
            }
            
            // Validar que el registro profesional no existe
            if (medicoRepository.existsByRegistroProfesional(medico.getRegistroProfesional())) {
                return ResponseEntity.badRequest().build();
            }
            
            Medico nuevoMedico = medicoRepository.save(medico);
            return ResponseEntity.ok(nuevoMedico);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medico> actualizarMedico(@PathVariable Long id, @RequestBody Medico medicoActualizado) {
        Optional<Medico> medicoExistente = medicoRepository.findById(id);
        if (medicoExistente.isPresent()) {
            Medico medico = medicoExistente.get();
            medico.setNombre(medicoActualizado.getNombre());
            medico.setApellido(medicoActualizado.getApellido());
            medico.setEmail(medicoActualizado.getEmail());
            medico.setTelefono(medicoActualizado.getTelefono());
            medico.setEspecialidad(medicoActualizado.getEspecialidad());
            medico.setRegistroProfesional(medicoActualizado.getRegistroProfesional());
            if (medicoActualizado.getContrasena() != null) {
                medico.setContrasena(medicoActualizado.getContrasena());
            }
            
            Medico medicoGuardado = medicoRepository.save(medico);
            return ResponseEntity.ok(medicoGuardado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedico(@PathVariable Long id) {
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
