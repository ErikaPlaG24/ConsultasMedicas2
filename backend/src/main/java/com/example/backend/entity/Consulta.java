package com.example.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
public class Consulta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id")
    private Paciente paciente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private Medico medico;
    
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;
    
    @Column(length = 50)
    private String estado = "PENDIENTE";
    
    @Column(name = "notas_medicas", columnDefinition = "TEXT")
    private String notasMedicas;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructores
    public Consulta() {}
    
    public Consulta(Paciente paciente, Medico medico, LocalDateTime fechaHora, String motivo) {
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
    public Medico getMedico() {
        return medico;
    }
    
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
    
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getNotasMedicas() {
        return notasMedicas;
    }
    
    public void setNotasMedicas(String notasMedicas) {
        this.notasMedicas = notasMedicas;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Consulta{" +
                "id=" + id +
                ", fechaHora=" + fechaHora +
                ", motivo='" + motivo + '\'' +
                ", estado='" + estado + '\'' +
                ", notasMedicas='" + notasMedicas + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
