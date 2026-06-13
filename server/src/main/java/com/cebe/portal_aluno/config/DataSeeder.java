package com.cebe.portal_aluno.config;

import com.cebe.portal_aluno.entity.Admin;
import com.cebe.portal_aluno.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin admin = Admin.builder()
                        .nome("Secretaria CEBE")
                        .email("admin@cebe.com")
                        // Senha em texto claro aqui para o seeder, vai ser codificada
                        .senha(passwordEncoder.encode("admin123"))
                        .build();
                adminRepository.save(admin);
                System.out.println("Admin default criado: admin@cebe.com / admin123");
            }
        };
    }
}
