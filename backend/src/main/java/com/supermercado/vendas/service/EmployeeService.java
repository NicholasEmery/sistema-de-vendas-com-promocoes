package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.employee.EmployeeCreateRequest;
import com.supermercado.vendas.dto.employee.EmployeeResponse;
import com.supermercado.vendas.dto.employee.EmployeeUpdateRequest;
import com.supermercado.vendas.exception.BusinessException;
import com.supermercado.vendas.exception.ResourceNotFoundException;
import com.supermercado.vendas.model.EmployeeUser;
import com.supermercado.vendas.model.Role;
import com.supermercado.vendas.repository.EmployeeUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeUserRepository employeeUserRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeResponse create(EmployeeCreateRequest request) {
        validateCreate(request);

        EmployeeUser employee = EmployeeUser.builder()
                .name(request.name())
                .cpf(request.cpf())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(request.roles() == null || request.roles().isEmpty() ? Set.of(Role.ROLE_EMPLOYEE) : request.roles())
                .build();

        return toResponse(employeeUserRepository.save(employee));
    }

    public List<EmployeeResponse> findAll() {
        return employeeUserRepository.findAll().stream().map(this::toResponse).toList();
    }

    public EmployeeResponse findById(String id) {
        return toResponse(findEntityById(id));
    }

    public EmployeeResponse update(String id, EmployeeUpdateRequest request) {
        EmployeeUser employee = findEntityById(id);

        if (request.username() != null && !request.username().equals(employee.getUsername())
                && employeeUserRepository.existsByUsername(request.username())) {
            throw new BusinessException("Já existe funcionário com este username");
        }

        if (request.cpf() != null && !request.cpf().equals(employee.getCpf())
                && employeeUserRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe funcionário com este CPF");
        }

        if (request.name() != null && !request.name().isBlank()) {
            employee.setName(request.name());
        }
        if (request.cpf() != null && !request.cpf().isBlank()) {
            employee.setCpf(request.cpf());
        }
        if (request.username() != null && !request.username().isBlank()) {
            employee.setUsername(request.username());
        }
        if (request.password() != null && !request.password().isBlank()) {
            employee.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.roles() != null && !request.roles().isEmpty()) {
            employee.setRoles(request.roles());
        }

        return toResponse(employeeUserRepository.save(employee));
    }

    public void delete(String id) {
        EmployeeUser employee = findEntityById(id);
        employeeUserRepository.delete(employee);
    }

    public EmployeeUser findEntityById(String id) {
        return employeeUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado"));
    }

    private void validateCreate(EmployeeCreateRequest request) {
        if (employeeUserRepository.existsByUsername(request.username())) {
            throw new BusinessException("Já existe funcionário com este username");
        }
        if (employeeUserRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe funcionário com este CPF");
        }
    }

    private EmployeeResponse toResponse(EmployeeUser employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getCpf(),
                employee.getUsername(),
                employee.getRoles()
        );
    }
}
