package com.example.backend.repository;

import com.example.backend.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    
    List<Consulta> findByPacienteId(Long pacienteId);
    
    List<Consulta> findByMedicoId(Long medicoId);
    
    List<Consulta> findByEstado(String estado);
    
    @Query("SELECT c FROM Consulta c WHERE c.fechaHora BETWEEN :inicio AND :fin")
    List<Consulta> findByFechaHoraBetween(@Param("inicio") LocalDateTime inicio, 
                                          @Param("fin") LocalDateTime fin);
    
    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :medicoId AND c.fechaHora BETWEEN :inicio AND :fin")
    List<Consulta> findByMedicoIdAndFechaHoraBetween(@Param("medicoId") Long medicoId, 
                                                     @Param("inicio") LocalDateTime inicio, 
                                                     @Param("fin") LocalDateTime fin);
    
    @Query("SELECT c FROM Consulta c WHERE c.paciente.id = :pacienteId AND c.fechaHora BETWEEN :inicio AND :fin")
    List<Consulta> findByPacienteIdAndFechaHoraBetween(@Param("pacienteId") Long pacienteId, 
                                                       @Param("inicio") LocalDateTime inicio, 
                                                       @Param("fin") LocalDateTime fin);
}
