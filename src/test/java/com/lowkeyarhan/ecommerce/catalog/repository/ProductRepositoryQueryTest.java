package com.lowkeyarhan.ecommerce.catalog.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.lowkeyarhan.ecommerce.catalog.model.Category;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
class ProductRepositoryQueryTest {
    @Autowired private ProductRepository products;
    @Autowired private CategoryRepository categories;
    @Autowired private SellerRepository sellers;
    private Long categoryId;

    @BeforeEach
    void setUp() {
        Seller seller = new Seller();
        seller.setSellerName("Query Test Seller");
        seller.setContactEmail("query-seller@example.test");
        seller = sellers.saveAndFlush(seller);

        Category category = new Category();
        category.setCategoryName("Query Test Category");
        category = categories.saveAndFlush(category);
        categoryId = category.getId();

        Product lowStock = product(seller, category, "QUERY-LOW", "Query Low Stock", 2, "49.00");
        Product regular = product(seller, category, "QUERY-REG", "Query Regular Stock", 30, "99.00");
        products.saveAllAndFlush(java.util.List.of(lowStock, regular));
    }

    @Test
    void customCatalogueQueryFiltersByKeywordAndCategory() {
        var page = products.searchCatalogue("low", categoryId, null, false, PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().getFirst().getSku()).isEqualTo("QUERY-LOW");
    }

    @Test
    void customLowStockQueryUsesReorderThreshold() {
        var result = products.findLowStockProducts();
        assertThat(result).extracting(Product::getSku).contains("QUERY-LOW").doesNotContain("QUERY-REG");
    }

    private Product product(Seller seller, Category category, String sku, String name, int stock, String price) {
        Product product = new Product();
        product.setSeller(seller); product.setCategory(category); product.setSku(sku);
        product.setProductName(name); product.setUnitPrice(new BigDecimal(price));
        product.setStockQuantity(stock); product.setReorderLevel(5); product.setActive(true);
        return product;
    }
}
