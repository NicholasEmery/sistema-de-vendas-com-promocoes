package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.purchase.PurchaseRequest;
import com.supermercado.vendas.dto.purchase.PurchaseResponse;
import com.supermercado.vendas.exception.ResourceNotFoundException;
import com.supermercado.vendas.model.Purchase;
import com.supermercado.vendas.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CustomerService customerService;

    public PurchaseResponse create(PurchaseRequest request) {
        customerService.findEntityById(request.customerId());

        Purchase purchase = Purchase.builder()
                .customerId(request.customerId())
                .productName(request.productName())
                .price(request.price())
                .purchaseDate(request.purchaseDate())
                .productType(request.productType())
                .build();

        return toResponse(purchaseRepository.save(purchase));
    }

    public List<PurchaseResponse> findAll() {
        return purchaseRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PurchaseResponse findById(String id) {
        return toResponse(findEntityById(id));
    }

    public List<PurchaseResponse> findByCustomerId(String customerId) {
        customerService.findEntityById(customerId);
        return purchaseRepository.findByCustomerId(customerId).stream().map(this::toResponse).toList();
    }

    public PurchaseResponse update(String id, PurchaseRequest request) {
        customerService.findEntityById(request.customerId());

        Purchase purchase = findEntityById(id);
        purchase.setCustomerId(request.customerId());
        purchase.setProductName(request.productName());
        purchase.setPrice(request.price());
        purchase.setPurchaseDate(request.purchaseDate());
        purchase.setProductType(request.productType());

        return toResponse(purchaseRepository.save(purchase));
    }

    public void delete(String id) {
        Purchase purchase = findEntityById(id);
        purchaseRepository.delete(purchase);
    }

    public List<Purchase> findEntitiesByCustomerId(String customerId) {
        return purchaseRepository.findByCustomerId(customerId);
    }

    public Purchase findEntityById(String id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra não encontrada"));
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        return new PurchaseResponse(
                purchase.getId(),
                purchase.getCustomerId(),
                purchase.getProductName(),
                purchase.getPrice(),
                purchase.getPurchaseDate(),
                purchase.getProductType()
        );
    }
}
