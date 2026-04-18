package com.supermercado.vendas.controller;

import com.supermercado.vendas.dto.promotion.PromotionResponse;
import com.supermercado.vendas.service.PersonalizedPromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
public class PersonalizedPromotionController {

    private final PersonalizedPromotionService personalizedPromotionService;

    @GetMapping("/{customerId}/personalized-promotions")
    public ResponseEntity<List<PromotionResponse>> getPersonalizedPromotions(@PathVariable String customerId) {
        return ResponseEntity.ok(personalizedPromotionService.getPromotionsForCustomer(customerId));
    }
}
