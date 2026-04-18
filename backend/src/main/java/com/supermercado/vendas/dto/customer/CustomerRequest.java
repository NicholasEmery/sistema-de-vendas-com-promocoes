package com.supermercado.vendas.dto.customer;

import com.supermercado.vendas.model.ProductType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record CustomerRequest(
        @NotBlank(message = "Nome do cliente é obrigatório")
        String name,

        @NotBlank(message = "CPF do cliente é obrigatório")
        String cpf,

        @NotNull(message = "Idade é obrigatória")
        @Min(value = 0, message = "Idade não pode ser negativa")
        @Max(value = 130, message = "Idade inválida")
        Integer age,

        Set<ProductType> favoriteProductTypes
) {
}
