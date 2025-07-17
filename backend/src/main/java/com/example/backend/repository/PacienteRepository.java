package com.example.backend.repository;

import com.example.backend.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
    Optional<Paciente> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<Paciente> findByEmailAndContrasena(String email, String contrasena);
}
