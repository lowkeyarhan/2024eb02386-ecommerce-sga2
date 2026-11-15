package com.lowkeyarhan.ecommerce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.lowkeyarhan.ecommerce.catalog.model.Category;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import com.lowkeyarhan.ecommerce.catalog.repository.CategoryRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.SellerRepository;
import com.lowkeyarhan.ecommerce.customer.model.Customer;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EndpointSmokeTest {
    @Autowired private MockMvc mvc;
    @Autowired private CustomerRepository customers;
    @Autowired private SellerRepository sellers;
    @Autowired private CategoryRepository categories;
    @Autowired private ProductRepository products;
    private Long customerId;
    private Long productId;

    @BeforeEach
    void seed() {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        Customer customer = new Customer();
        customer.setFirstName("Endpoint"); customer.setLastName("Tester"); customer.setEmail("endpoint-" + unique + "@example.test");
        customer = customers.saveAndFlush(customer);
        customerId = customer.getId();

        Seller seller = new Seller(); seller.setSellerName("Endpoint Seller " + unique); seller.setContactEmail("endpoint-seller-" + unique + "@example.test");
        seller = sellers.saveAndFlush(seller);
        Category category = new Category(); category.setCategoryName("Endpoint Category " + unique);
        category = categories.saveAndFlush(category);
        Product product = new Product(); product.setSeller(seller); product.setCategory(category); product.setSku("ENDPOINT-" + unique);
        product.setProductName("Endpoint Product"); product.setUnitPrice(new BigDecimal("199.00"));
        product.setStockQuantity(10); product.setReorderLevel(2); product.setActive(true);
        productId = products.saveAndFlush(product).getId();
    }

    @Test
    void healthEndpointReportsApplicationStatus() throws Exception {
        mvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void stockEndpointReturnsCurrentProductQuantity() throws Exception {
        mvc.perform(get("/api/products/{id}/stock", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void ajaxCartEndpointAddsTheRequestedQuantity() throws Exception {
        mvc.perform(post("/api/cart/items").param("customerId", customerId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":" + productId + ",\"quantity\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemCount").value(2));
    }
}
