package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.promotion.PromotionRequest;
import com.supermercado.vendas.dto.promotion.PromotionResponse;
import com.supermercado.vendas.exception.BusinessException;
import com.supermercado.vendas.exception.ResourceNotFoundException;
import com.supermercado.vendas.model.Promotion;
import com.supermercado.vendas.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionResponse create(PromotionRequest request) {
        validateDateRange(request.startDate(), request.endDate());

        Promotion promotion = Promotion.builder()
                .name(request.name())
                .description(request.description())
                .targetProductTypes(request.targetProductTypes())
                .discountPercent(request.discountPercent())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .customerCpfTargets(request.customerCpfTargets() == null ? new HashSet<>() : new HashSet<>(request.customerCpfTargets()))
                .build();

        return toResponse(promotionRepository.save(promotion));
    }

    public List<PromotionResponse> findAll() {
        return promotionRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PromotionResponse findById(String id) {
        return toResponse(findEntityById(id));
    }

    public PromotionResponse update(String id, PromotionRequest request) {
        validateDateRange(request.startDate(), request.endDate());

        Promotion promotion = findEntityById(id);
        promotion.setName(request.name());
        promotion.setDescription(request.description());
        promotion.setTargetProductTypes(request.targetProductTypes());
        promotion.setDiscountPercent(request.discountPercent());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setCustomerCpfTargets(request.customerCpfTargets() == null ? new HashSet<>() : new HashSet<>(request.customerCpfTargets()));

        return toResponse(promotionRepository.save(promotion));
    }

    public void delete(String id) {
        Promotion promotion = findEntityById(id);
        promotionRepository.delete(promotion);
    }

    public List<Promotion> findActivePromotions() {
        LocalDate today = LocalDate.now();
        return promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }

    public Promotion findEntityById(String id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoção não encontrada"));
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessException("Data final não pode ser menor que data inicial");
        }
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
