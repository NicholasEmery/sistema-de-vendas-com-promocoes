package com.supermercado.vendas.dto.auth;

import java.util.Set;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        String username,
        Set<String> roles
) {
}
