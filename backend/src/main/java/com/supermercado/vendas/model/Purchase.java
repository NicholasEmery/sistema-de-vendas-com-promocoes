package com.supermercado.vendas.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchases")
public class Purchase {

    @Id
    private String id;

    private String customerId;

    private String productName;

    private BigDecimal price;

    private LocalDateTime purchaseDate;

    private ProductType productType;
}
