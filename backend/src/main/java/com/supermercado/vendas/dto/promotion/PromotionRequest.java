package com.supermercado.vendas.dto.promotion;

import com.supermercado.vendas.model.ProductType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record PromotionRequest(
        @NotBlank(message = "Nome da promoção é obrigatório")
        String name,

        @NotBlank(message = "Descrição da promoção é obrigatória")
        String description,

        @NotNull(message = "Tipos de produto alvo são obrigatórios")
        Set<ProductType> targetProductTypes,

        @NotNull(message = "Percentual de desconto é obrigatório")
        @DecimalMin(value = "0.01", message = "Desconto deve ser maior que 0")
        @DecimalMax(value = "100.00", message = "Desconto deve ser no máximo 100")
        BigDecimal discountPercent,

        @NotNull(message = "Data de início é obrigatória")
        LocalDate startDate,

        @NotNull(message = "Data de término é obrigatória")
        LocalDate endDate,

        Set<String> customerCpfTargets
) {
}
