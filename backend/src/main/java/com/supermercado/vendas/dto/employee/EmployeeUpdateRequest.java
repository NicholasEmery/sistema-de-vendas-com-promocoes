package com.supermercado.vendas.dto.employee;

import com.supermercado.vendas.model.Role;

import java.util.Set;

public record EmployeeUpdateRequest(
        String name,
        String cpf,
        String username,
        String password,
        Set<Role> roles
) {
}
