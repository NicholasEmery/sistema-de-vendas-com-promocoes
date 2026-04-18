package com.supermercado.vendas.dto.customer;

import com.supermercado.vendas.model.ProductType;

import java.util.Set;

public record CustomerResponse(
        String id,
        String name,
        String cpf,
        Integer age,
        Set<ProductType> favoriteProductTypes
) {
}
