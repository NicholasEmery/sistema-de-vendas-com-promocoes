package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.promotion.PromotionResponse;
import com.supermercado.vendas.model.Customer;
import com.supermercado.vendas.model.ProductType;
import com.supermercado.vendas.model.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalizedPromotionService {

    private final CustomerService customerService;
    private final PurchaseService purchaseService;
    private final PromotionService promotionService;

    public List<PromotionResponse> getPromotionsForCustomer(String customerId) {
        Customer customer = customerService.findEntityById(customerId);

        Set<ProductType> preferredTypes = new HashSet<>(customer.getFavoriteProductTypes());

        Map<ProductType, Long> purchasesByType = purchaseService.findEntitiesByCustomerId(customerId).stream()
                .collect(Collectors.groupingBy(
                        purchase -> purchase.getProductType(),
                        Collectors.counting()
                ));

        purchasesByType.entrySet().stream()
                .sorted(Map.Entry.<ProductType, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .forEach(preferredTypes::add);

        return promotionService.findActivePromotions().stream()
                .filter(promotion -> isPromotionApplicable(customer, preferredTypes, promotion))
                .sorted(Comparator.comparing(Promotion::getDiscountPercent).reversed())
                .map(this::toResponse)
                .toList();
    }

    private boolean isPromotionApplicable(Customer customer, Set<ProductType> preferredTypes, Promotion promotion) {
        if (promotion.getCustomerCpfTargets() != null
                && !promotion.getCustomerCpfTargets().isEmpty()
                && !promotion.getCustomerCpfTargets().contains(customer.getCpf())) {
            return false;
        }

        if (promotion.getTargetProductTypes() == null || promotion.getTargetProductTypes().isEmpty()) {
            return true;
        }

        return promotion.getTargetProductTypes().stream().anyMatch(preferredTypes::contains);
    }

    private PromotionResponse toResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getDescription(),
                promotion.getTargetProductTypes(),
                promotion.getDiscountPercent(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getCustomerCpfTargets()
        );
    }
}
