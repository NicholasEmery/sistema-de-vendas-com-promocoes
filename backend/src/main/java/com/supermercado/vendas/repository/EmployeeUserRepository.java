package com.supermercado.vendas.repository;

import com.supermercado.vendas.model.EmployeeUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployeeUserRepository extends MongoRepository<EmployeeUser, String> {

    Optional<EmployeeUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByCpf(String cpf);
}
