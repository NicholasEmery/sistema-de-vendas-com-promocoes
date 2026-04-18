package com.supermercado.vendas.repository;

import com.supermercado.vendas.model.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PurchaseRepository extends MongoRepository<Purchase, String> {

    List<Purchase> findByCustomerId(String customerId);
}
