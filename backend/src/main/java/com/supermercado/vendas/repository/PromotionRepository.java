package com.supermercado.vendas.repository;

import com.supermercado.vendas.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface PromotionRepository extends MongoRepository<Promotion, String> {

    List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);
}
