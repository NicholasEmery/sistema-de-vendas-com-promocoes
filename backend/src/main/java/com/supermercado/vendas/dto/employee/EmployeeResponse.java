package com.supermercado.vendas.dto.employee;

import com.supermercado.vendas.model.Role;

import java.util.Set;

public record EmployeeResponse(
        String id,
        String name,
        String cpf,
        String username,
        Set<Role> roles
) {
}
