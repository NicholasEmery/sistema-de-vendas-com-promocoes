package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.auth.LoginRequest;
import com.supermercado.vendas.dto.auth.LoginResponse;
import com.supermercado.vendas.model.EmployeeUser;
import com.supermercado.vendas.repository.EmployeeUserRepository;
import com.supermercado.vendas.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeUserRepository employeeUserRepository;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        EmployeeUser employee = employeeUserRepository.findByUsername(request.username())
                .orElseThrow();

        String token = jwtService.generateToken(employee);
        Set<String> roles = employee.getRoles().stream().map(Enum::name).collect(Collectors.toSet());

        return new LoginResponse(
                token,
                "Bearer",
                jwtService.getExpirationMs() / 1000,
                employee.getUsername(),
                roles
        );
    }
}
