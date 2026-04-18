package com.supermercado.vendas.repository;

import com.supermercado.vendas.model.Product;
import com.supermercado.vendas.model.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByType(ProductType type);
}
