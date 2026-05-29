package com.cebe.portal_aluno.controller;

import com.cebe.portal_aluno.dto.request.LoginRequestDTO;
import com.cebe.portal_aluno.dto.response.LoginResponseDTO;
import com.cebe.portal_aluno.entity.Aluno;
import com.cebe.portal_aluno.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

        Authentication auth = authenticationManager.authenticate(authenticationToken);
        Aluno principal = (Aluno) auth.getPrincipal();

        String token = tokenService.generateToken(principal);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
