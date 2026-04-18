package com.supermercado.vendas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "promotions")
public class Promotion {

    @Id
    private String id;

    private String name;

    private String description;

    @Builder.Default
    private Set<ProductType> targetProductTypes = new HashSet<>();

    private BigDecimal discountPercent;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Set<String> customerCpfTargets = new HashSet<>();
}
