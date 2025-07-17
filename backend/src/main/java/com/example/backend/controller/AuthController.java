package com.example.backend.controller;

import com.example.backend.entity.Paciente;
import com.example.backend.entity.Medico;
import com.example.backend.repository.PacienteRepository;
import com.example.backend.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // DTO para login
    public static class LoginRequest {
        private String email;
        private String contrasena;
        private String tipoUsuario; // "paciente" o "medico"

        // Getters y setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
        public String getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    }

    // DTO para respuesta de login
    public static class LoginResponse {
        private boolean success;
        private String message;
        private String tipoUsuario;
        private Long userId;
        private String nombre;
        private String apellido;
        private String email;
        private boolean isAdmin;

        public LoginResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
            this.isAdmin = false; // Por defecto no es admin
        }

        // Getters y setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getTipoUsuario() { return tipoUsuario; }
        public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public boolean getIsAdmin() { return isAdmin; }
        public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            if (request.getEmail() == null || request.getContrasena() == null) {
                return ResponseEntity.badRequest().body(
                    new LoginResponse(false, "Email y contraseña son obligatorios")
                );
            }

            // Si no se especifica el tipo de usuario, intentar autenticar en ambas tablas
            if (request.getTipoUsuario() == null || request.getTipoUsuario().isEmpty()) {
                // Intentar primero como médico
                Optional<Medico> medicoOpt = medicoRepository.findByEmailAndContrasena(
                    request.getEmail(), request.getContrasena()
                );
                
                if (medicoOpt.isPresent()) {
                    Medico medico = medicoOpt.get();
                    LoginResponse response = new LoginResponse(true, "Login exitoso");
                    response.setTipoUsuario("medico");
                    response.setUserId(medico.getId());
                    response.setNombre(medico.getNombre());
                    response.setApellido(medico.getApellido());
                    response.setEmail(medico.getEmail());
                    
                    // Verificar si es Fernando Castillo (admin)
                    if ("f.castillo@hospital.com".equals(medico.getEmail())) {
                        response.setIsAdmin(true);
                    }
                    
                    return ResponseEntity.ok(response);
                }
                
                // Si no es médico, intentar como paciente
                Optional<Paciente> pacienteOpt = pacienteRepository.findByEmailAndContrasena(
                    request.getEmail(), request.getContrasena()
                );
                
                if (pacienteOpt.isPresent()) {
                    Paciente paciente = pacienteOpt.get();
                    LoginResponse response = new LoginResponse(true, "Login exitoso");
                    response.setTipoUsuario("paciente");
                    response.setUserId(paciente.getId());
                    response.setNombre(paciente.getNombre());
                    response.setApellido(paciente.getApellido());
                    response.setEmail(paciente.getEmail());
                    return ResponseEntity.ok(response);
                }
            } else {
                // Lógica original con tipo de usuario específico
                if ("paciente".equals(request.getTipoUsuario())) {
                    Optional<Paciente> pacienteOpt = pacienteRepository.findByEmailAndContrasena(
                        request.getEmail(), request.getContrasena()
                    );
                    
                    if (pacienteOpt.isPresent()) {
                        Paciente paciente = pacienteOpt.get();
                        LoginResponse response = new LoginResponse(true, "Login exitoso");
                        response.setTipoUsuario("paciente");
                        response.setUserId(paciente.getId());
                        response.setNombre(paciente.getNombre());
                        response.setApellido(paciente.getApellido());
                        response.setEmail(paciente.getEmail());
                        return ResponseEntity.ok(response);
                    }
                } else if ("medico".equals(request.getTipoUsuario())) {
                    Optional<Medico> medicoOpt = medicoRepository.findByEmailAndContrasena(
                        request.getEmail(), request.getContrasena()
                    );
                    
                    if (medicoOpt.isPresent()) {
                        Medico medico = medicoOpt.get();
                        LoginResponse response = new LoginResponse(true, "Login exitoso");
                        response.setTipoUsuario("medico");
                        response.setUserId(medico.getId());
                        response.setNombre(medico.getNombre());
                        response.setApellido(medico.getApellido());
                        response.setEmail(medico.getEmail());
                        
                        // Verificar si es Fernando Castillo (admin)
                        if ("f.castillo@hospital.com".equals(medico.getEmail())) {
                            response.setIsAdmin(true);
                        }
                        
                        return ResponseEntity.ok(response);
                    }
                }
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new LoginResponse(false, "Credenciales inválidas")
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponse(false, "Error interno del servidor")
            );
        }
    }

    // Registro de paciente
    @PostMapping("/register/paciente")
    public ResponseEntity<LoginResponse> registerPaciente(@RequestBody Paciente paciente) {
        try {
            // Validar que el email no existe
            if (pacienteRepository.existsByEmail(paciente.getEmail())) {
                return ResponseEntity.badRequest().body(
                    new LoginResponse(false, "El email ya está registrado")
                );
            }

            // Validar campos obligatorios
            if (paciente.getNombre() == null || paciente.getApellido() == null || 
                paciente.getEmail() == null || paciente.getContrasena() == null) {
                return ResponseEntity.badRequest().body(
                    new LoginResponse(false, "Todos los campos son obligatorios")
                );
            }

            Paciente nuevoPaciente = pacienteRepository.save(paciente);
            
            LoginResponse response = new LoginResponse(true, "Registro exitoso");
            response.setTipoUsuario("paciente");
            response.setUserId(nuevoPaciente.getId());
            response.setNombre(nuevoPaciente.getNombre());
            response.setApellido(nuevoPaciente.getApellido());
            response.setEmail(nuevoPaciente.getEmail());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponse(false, "Error al registrar paciente")
            );
        }
    }

    // Registro de médico
    @PostMapping("/register/medico")
    public ResponseEntity<LoginResponse> registerMedico(@RequestBody Medico medico) {
        try {
            // Validar que el email no existe
            if (medicoRepository.existsByEmail(medico.getEmail())) {
                return ResponseEntity.badRequest().body(
                    new LoginResponse(false, "El email ya está registrado")
                );
            }

            // Validar que el registro profesional no existe
            if (medicoRepository.existsByRegistroProfesional(medico.getRegistroProfesional())) {
                return ResponseEntity.badRequest().body(
                    new LoginResponse(false, "El registro profesional ya está registrado")
                );
            }

            // Validar campos obligatorios
            if (medico.getNombre() == null || medico.getApellido() == null || 
                medico.getEmail() == null || medico.getContrasena() == null ||
                medico.getEspecialidad() == null || medico.getRegistroProfesional() == null) {
                return ResponseEntity.badRequest().body(
                    new LoginResponse(false, "Todos los campos son obligatorios")
                );
            }

            Medico nuevoMedico = medicoRepository.save(medico);
            
            LoginResponse response = new LoginResponse(true, "Registro exitoso");
            response.setTipoUsuario("medico");
            response.setUserId(nuevoMedico.getId());
            response.setNombre(nuevoMedico.getNombre());
            response.setApellido(nuevoMedico.getApellido());
            response.setEmail(nuevoMedico.getEmail());
            
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponse(false, "Error al registrar médico")
            );
        }
    }

    // Validar sesión
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateSession(@RequestParam String email, 
                                                               @RequestParam String tipoUsuario) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if ("paciente".equals(tipoUsuario)) {
                Optional<Paciente> pacienteOpt = pacienteRepository.findByEmail(email);
                if (pacienteOpt.isPresent()) {
                    response.put("valid", true);
                    response.put("user", pacienteOpt.get());
                    return ResponseEntity.ok(response);
                }
            } else if ("medico".equals(tipoUsuario)) {
                Optional<Medico> medicoOpt = medicoRepository.findByEmail(email);
                if (medicoOpt.isPresent()) {
                    response.put("valid", true);
                    response.put("user", medicoOpt.get());
                    return ResponseEntity.ok(response);
                }
            }
            
            response.put("valid", false);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("valid", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
