package com.supermercado.vendas.config;

import com.supermercado.vendas.model.EmployeeUser;
import com.supermercado.vendas.model.Role;
import com.supermercado.vendas.repository.EmployeeUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    public CommandLineRunner bootstrapAdmin(
            EmployeeUserRepository employeeUserRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.name}") String name,
            @Value("${app.bootstrap.admin.cpf}") String cpf,
            @Value("${app.bootstrap.admin.username}") String username,
            @Value("${app.bootstrap.admin.password}") String password
    ) {
        return args -> {
            try {
                if (employeeUserRepository.count() == 0) {
                    EmployeeUser admin = EmployeeUser.builder()
                            .name(name)
                            .cpf(cpf)
                            .username(username)
                            .password(passwordEncoder.encode(password))
                            .roles(Set.of(Role.ROLE_ADMIN))
                            .build();

                    employeeUserRepository.save(admin);
                    log.warn("Usuário admin inicial criado com username: {}. Altere a senha padrão imediatamente.", username);
                }
            } catch (DataAccessException ex) {
                log.error("Não foi possível inicializar o usuário admin. Verifique a conexão com o MongoDB.", ex);
            }
        };
    }
}
