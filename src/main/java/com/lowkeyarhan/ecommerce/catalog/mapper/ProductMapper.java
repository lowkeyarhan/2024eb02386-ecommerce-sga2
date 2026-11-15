package com.lowkeyarhan.ecommerce.catalog.mapper;

import com.lowkeyarhan.ecommerce.catalog.dto.ProductForm;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductForm toForm(Product product) {
        ProductForm form = new ProductForm();
        form.setId(product.getId()); form.setSku(product.getSku()); form.setProductName(product.getProductName());
        form.setDescription(product.getDescription()); form.setUnitPrice(product.getUnitPrice());
        form.setStockQuantity(product.getStockQuantity()); form.setReorderLevel(product.getReorderLevel());
        form.setActive(product.isActive()); form.setSellerId(product.getSeller().getId()); form.setCategoryId(product.getCategory().getId());
        return form;
    }
}
