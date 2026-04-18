package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.customer.CustomerRequest;
import com.supermercado.vendas.dto.customer.CustomerResponse;
import com.supermercado.vendas.exception.BusinessException;
import com.supermercado.vendas.exception.ResourceNotFoundException;
import com.supermercado.vendas.model.Customer;
import com.supermercado.vendas.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe cliente com este CPF");
        }

        Customer customer = Customer.builder()
                .name(request.name())
                .cpf(request.cpf())
                .age(request.age())
                .favoriteProductTypes(request.favoriteProductTypes() == null ? new HashSet<>() : new HashSet<>(request.favoriteProductTypes()))
                .build();

        return toResponse(customerRepository.save(customer));
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CustomerResponse findById(String id) {
        return toResponse(findEntityById(id));
    }

    public CustomerResponse update(String id, CustomerRequest request) {
        Customer customer = findEntityById(id);

        if (!customer.getCpf().equals(request.cpf()) && customerRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("Já existe cliente com este CPF");
        }

        customer.setName(request.name());
        customer.setCpf(request.cpf());
        customer.setAge(request.age());
        customer.setFavoriteProductTypes(request.favoriteProductTypes() == null ? new HashSet<>() : new HashSet<>(request.favoriteProductTypes()));

        return toResponse(customerRepository.save(customer));
    }

    public void delete(String id) {
        Customer customer = findEntityById(id);
        customerRepository.delete(customer);
    }

    public Customer findEntityById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getAge(),
                customer.getFavoriteProductTypes()
        );
    }
}
