package com.lowkeyarhan.ecommerce.cart.service;

import com.lowkeyarhan.ecommerce.cart.model.Cart;
import com.lowkeyarhan.ecommerce.cart.model.CartItem;
import com.lowkeyarhan.ecommerce.cart.dto.CartItemView;
import com.lowkeyarhan.ecommerce.cart.dto.CartPageView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.lowkeyarhan.ecommerce.cart.repository.CartItemRepository;
import com.lowkeyarhan.ecommerce.cart.repository.CartRepository;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import com.lowkeyarhan.ecommerce.common.validation.BusinessRules;
import com.lowkeyarhan.ecommerce.customer.model.Customer;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class CartService implements CartServicePort {
    private final CartRepository carts; private final CartItemRepository items; private final CustomerRepository customers;
    private final ProductRepository products; private final BusinessRules rules;

    @Transactional public Cart getOrCreateOpenCart(Long customerId) {
        return carts.findFirstByCustomer_IdAndStatusOrderByCreatedAtDesc(customerId, "OPEN").orElseGet(() -> {
            Customer customer = customers.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
            Cart cart = new Cart(); cart.setCustomer(customer); cart.setStatus("OPEN"); return carts.save(cart);
        });
    }
    @Transactional public Cart add(Long customerId, Long productId, Integer quantity) {
        rules.requirePositiveQuantity(quantity);
        getOrCreateOpenCart(customerId);
        Cart cart = carts.lockOpenCart(customerId).orElseThrow(() -> new IllegalStateException("Open cart could not be locked."));
        Product product = products.findByIdForUpdate(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        if (!product.isActive()) throw new IllegalArgumentException("This product is not available for sale.");
        CartItem item = items.findByCart_IdAndProduct_Id(cart.getId(), productId).orElseGet(() -> {
            CartItem created = new CartItem(); created.setCart(cart); created.setProduct(product); created.setQuantity(0); return created;
        });
        int nextQuantity = item.getQuantity() + quantity;
        rules.requireAvailableStock(nextQuantity, product.getStockQuantity());
        item.setQuantity(nextQuantity); items.save(item); return getOrCreateOpenCart(customerId);
    }
    @Transactional public Cart update(Long customerId, Long itemId, Integer quantity) {
        rules.requirePositiveQuantity(quantity);
        getOrCreateOpenCart(customerId);
        Cart cart = carts.lockOpenCart(customerId).orElseThrow(() -> new IllegalStateException("Open cart could not be locked."));
        CartItem item = items.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found."));
        if (!item.getCart().getId().equals(cart.getId())) throw new IllegalArgumentException("Cart item does not belong to this cart.");
        rules.requireAvailableStock(quantity, item.getProduct().getStockQuantity()); item.setQuantity(quantity); items.save(item); return cart;
    }
    @Transactional public Cart remove(Long customerId, Long itemId) {
        getOrCreateOpenCart(customerId);
        Cart cart = carts.lockOpenCart(customerId).orElseThrow(() -> new IllegalStateException("Open cart could not be locked."));
        CartItem item = items.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found."));
        if (!item.getCart().getId().equals(cart.getId())) throw new IllegalArgumentException("Cart item does not belong to this cart.");
        items.delete(item); return cart;
    }
    @Transactional(readOnly=true) public Optional<Cart> findOpenCart(Long customerId) { return carts.findFirstByCustomer_IdAndStatusOrderByCreatedAtDesc(customerId, "OPEN"); }
    @Transactional(readOnly=true) public CartPageView view(Long customerId) {
        Optional<Cart> optional = carts.findFirstByCustomer_IdAndStatusOrderByCreatedAtDesc(customerId, "OPEN");
        if (optional.isEmpty()) return new CartPageView(null, customerId, List.of(), BigDecimal.ZERO, 0);
        Cart cart = optional.get();
        List<CartItemView> rows = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        int count = 0;
        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            BigDecimal lineTotal = product.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal); count += item.getQuantity();
            rows.add(CartItemView.builder().id(item.getId()).productId(product.getId()).sku(product.getSku())
                .productName(product.getProductName()).quantity(item.getQuantity()).stockQuantity(product.getStockQuantity())
                .unitPrice(product.getUnitPrice()).lineTotal(lineTotal).build());
        }
        return new CartPageView(cart.getId(), customerId, rows, subtotal, count);
    }
}
