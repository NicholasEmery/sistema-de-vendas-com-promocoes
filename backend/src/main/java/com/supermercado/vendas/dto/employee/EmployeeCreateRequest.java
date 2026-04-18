package com.supermercado.vendas.dto.employee;

import com.supermercado.vendas.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record EmployeeCreateRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "CPF é obrigatório")
        String cpf,

        @NotBlank(message = "Username é obrigatório")
        String username,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter ao menos 6 caracteres")
        String password,

        @NotNull(message = "Pelo menos um papel deve ser informado")
        Set<Role> roles
) {
}
