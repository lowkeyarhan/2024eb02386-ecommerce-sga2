package com.lowkeyarhan.ecommerce.catalog.service;

import com.lowkeyarhan.ecommerce.catalog.dto.ProductForm;
import com.lowkeyarhan.ecommerce.catalog.mapper.ProductMapper;
import com.lowkeyarhan.ecommerce.catalog.model.Category;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import com.lowkeyarhan.ecommerce.catalog.repository.CategoryRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.SellerRepository;
import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;
import com.lowkeyarhan.ecommerce.inventory.repository.InventoryMovementRepository;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import com.lowkeyarhan.ecommerce.common.validation.BusinessRules;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
@Transactional(readOnly=true)
public class ProductService implements ProductServicePort {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final ProductMapper mapper;
    private final BusinessRules rules;
    private final InventoryMovementRepository inventoryMovements;

    @Override public Page<Product> search(String keyword, Long categoryId, Long sellerId, boolean activeOnly, Pageable pageable) {
        return productRepository.searchCatalogue(clean(keyword), categoryId, sellerId, activeOnly, pageable);
    }
    @Override public Product get(Long id) { return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id)); }
    @Override public ProductForm form(Long id) { return mapper.toForm(get(id)); }
    @Override @Transactional public Product save(ProductForm form) {
        rules.requirePositivePrice(form.getUnitPrice());
        if (form.getStockQuantity() == null || form.getStockQuantity() < 0) throw new IllegalArgumentException("Stock cannot be negative.");
        Product product = form.getId() == null ? new Product() : get(form.getId());
        int previousStock = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
        Seller seller = sellerRepository.findById(form.getSellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found."));
        Category category = categoryRepository.findById(form.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found."));
        product.setSku(form.getSku().trim()); product.setProductName(form.getProductName().trim());
        product.setDescription(form.getDescription()); product.setUnitPrice(form.getUnitPrice());
        product.setStockQuantity(form.getStockQuantity()); product.setReorderLevel(form.getReorderLevel());
        product.setActive(form.isActive()); product.setSeller(seller); product.setCategory(category);
        Product saved = productRepository.saveAndFlush(product);
        int stockDelta = form.getStockQuantity() - previousStock;
        if (stockDelta != 0) {
            InventoryMovement movement = new InventoryMovement();
            movement.setProduct(saved); movement.setQuantityDelta(stockDelta); movement.setReason("ADJUSTMENT");
            movement.setNote(form.getId() == null ? "Opening stock from product creation" : "Manual catalogue stock adjustment");
            inventoryMovements.save(movement);
        }
        return saved;
    }
    @Override @Transactional public void delete(Long id) { productRepository.delete(get(id)); }
    private String clean(String value) { return value == null || value.isBlank() ? null : value.trim().toLowerCase(Locale.ROOT); }
}
