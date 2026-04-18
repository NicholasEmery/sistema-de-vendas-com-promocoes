package com.supermercado.vendas.service;

import com.supermercado.vendas.dto.product.ProductRequest;
import com.supermercado.vendas.dto.product.ProductResponse;
import com.supermercado.vendas.exception.BusinessException;
import com.supermercado.vendas.exception.ResourceNotFoundException;
import com.supermercado.vendas.model.Product;
import com.supermercado.vendas.model.ProductType;
import com.supermercado.vendas.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse create(ProductRequest request) {
        validatePriceConsistency(request.currentPrice(), request.promotionalPrice());

        Product product = Product.builder()
                .name(request.name())
                .currentPrice(request.currentPrice())
                .promotionalPrice(request.promotionalPrice())
                .type(request.type())
                .description(request.description())
                .expirationDate(request.expirationDate())
                .build();

        return toResponse(productRepository.save(product));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ProductResponse findById(String id) {
        return toResponse(findEntityById(id));
    }

    public ProductResponse update(String id, ProductRequest request) {
        validatePriceConsistency(request.currentPrice(), request.promotionalPrice());

        Product product = findEntityById(id);
        product.setName(request.name());
        product.setCurrentPrice(request.currentPrice());
        product.setPromotionalPrice(request.promotionalPrice());
        product.setType(request.type());
        product.setDescription(request.description());
        product.setExpirationDate(request.expirationDate());

        return toResponse(productRepository.save(product));
    }

    public void delete(String id) {
        Product product = findEntityById(id);
        productRepository.delete(product);
    }

    public List<ProductResponse> applyDiscountByType(ProductType type, BigDecimal discountPercent) {
        List<Product> products = productRepository.findByType(type);
        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum produto encontrado para o tipo informado");
        }

        BigDecimal discountRatio = discountPercent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        products.forEach(product -> {
            BigDecimal promotionalPrice = product.getCurrentPrice()
                    .multiply(BigDecimal.ONE.subtract(discountRatio))
                    .setScale(2, RoundingMode.HALF_UP);
            product.setPromotionalPrice(promotionalPrice);
        });

        return productRepository.saveAll(products).stream().map(this::toResponse).toList();
    }

    public Product findEntityById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    private void validatePriceConsistency(BigDecimal currentPrice, BigDecimal promotionalPrice) {
        if (promotionalPrice != null && promotionalPrice.compareTo(currentPrice) > 0) {
            throw new BusinessException("Preço promocional não pode ser maior que o preço atual");
        }
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCurrentPrice(),
                product.getPromotionalPrice(),
                product.getType(),
                product.getDescription(),
                product.getExpirationDate()
        );
    }
}
