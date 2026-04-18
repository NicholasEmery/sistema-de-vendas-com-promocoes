package com.supermercado.vendas.repository;

import com.supermercado.vendas.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
