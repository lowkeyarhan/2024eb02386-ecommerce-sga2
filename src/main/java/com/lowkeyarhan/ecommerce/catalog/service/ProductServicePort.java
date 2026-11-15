package com.lowkeyarhan.ecommerce.catalog.service;

import com.lowkeyarhan.ecommerce.catalog.dto.ProductForm;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductServicePort {
    Page<Product> search(String keyword, Long categoryId, Long sellerId, boolean activeOnly, Pageable pageable);
    Product get(Long id);
    ProductForm form(Long id);
    Product save(ProductForm form);
    void delete(Long id);
}
