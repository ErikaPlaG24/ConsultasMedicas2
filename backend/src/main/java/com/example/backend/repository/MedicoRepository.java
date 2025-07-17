package com.example.backend.repository;

import com.example.backend.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    
    Optional<Medico> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByRegistroProfesional(String registroProfesional);
    
    List<Medico> findByEspecialidad(String especialidad);
    
    Optional<Medico> findByEmailAndContrasena(String email, String contrasena);
}
