package com.supermercado.vendas.dto.product;

import com.supermercado.vendas.model.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductRequest(
        @NotBlank(message = "Nome do produto é obrigatório")
        String name,

        @NotNull(message = "Preço atual é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço atual deve ser maior que zero")
        BigDecimal currentPrice,

        @DecimalMin(value = "0.00", message = "Preço promocional não pode ser negativo")
        BigDecimal promotionalPrice,

        @NotNull(message = "Tipo do produto é obrigatório")
        ProductType type,

        @NotBlank(message = "Descrição é obrigatória")
        String description,

        @NotNull(message = "Data de validade é obrigatória")
        LocalDate expirationDate
) {
}
