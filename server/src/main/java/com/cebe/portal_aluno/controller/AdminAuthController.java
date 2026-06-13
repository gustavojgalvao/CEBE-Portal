package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.request.AdminLoginRequestDTO;
import com.cebe.portal_aluno.dto.response.LoginResponseDTO;
import com.cebe.portal_aluno.entity.Admin;
import com.cebe.portal_aluno.repository.AdminRepository;
import com.cebe.portal_aluno.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/admin")
@CrossOrigin("*")
public class AdminAuthController {

    private final AdminRepository adminRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AdminAuthController(AdminRepository adminRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequestDTO dto) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(dto.email());
        
        if (adminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin não encontrado.");
        }
        
        Admin admin = adminOpt.get();
        if (!passwordEncoder.matches(dto.senha(), admin.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
        }
        
        String token = tokenService.generateTokenAdmin(admin);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
