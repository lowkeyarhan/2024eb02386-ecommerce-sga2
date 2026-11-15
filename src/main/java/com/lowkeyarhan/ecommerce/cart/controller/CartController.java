package com.lowkeyarhan.ecommerce.cart.controller;

import com.lowkeyarhan.ecommerce.cart.dto.AddToCartRequest;
import com.lowkeyarhan.ecommerce.cart.service.CartServicePort;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerAddressRepository;
import com.lowkeyarhan.ecommerce.customer.service.CustomerServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class CartController {
    private final CartServicePort carts; private final CustomerServicePort customers; private final CustomerAddressRepository addresses;
    @GetMapping("/cart") public String view(@RequestParam(defaultValue="1") Long customerId, Model model) {
        model.addAttribute("cart", carts.view(customerId)); model.addAttribute("customers", customers.findAll());
        model.addAttribute("addresses", addresses.findByCustomer_IdOrderByDefaultAddressDescIdAsc(customerId));
        model.addAttribute("selectedCustomerId", customerId); return "cart/view";
    }
    @PostMapping("/api/cart/items") @ResponseBody public ResponseEntity<?> add(@Valid @RequestBody AddToCartRequest request, @RequestParam(defaultValue="1") Long customerId) {
        try {
            carts.add(customerId, request.getProductId(), request.getQuantity());
            var view = carts.view(customerId);
            return ResponseEntity.ok(java.util.Map.of("message", "Added to cart", "itemCount", view.itemCount(), "subtotal", view.subtotal()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("message", ex.getMessage() == null ? "Unable to update cart." : ex.getMessage()));
        }
    }
    @PostMapping("/cart/items/{itemId}/update") public String update(@PathVariable Long itemId, @RequestParam(defaultValue="1") Long customerId, @RequestParam Integer quantity, RedirectAttributes flash) {
        carts.update(customerId, itemId, quantity); flash.addFlashAttribute("success", "Cart quantity updated."); return "redirect:/cart?customerId=" + customerId;
    }
    @PostMapping("/cart/items/{itemId}/remove") public String remove(@PathVariable Long itemId, @RequestParam(defaultValue="1") Long customerId, RedirectAttributes flash) {
        carts.remove(customerId, itemId); flash.addFlashAttribute("success", "Item removed from cart."); return "redirect:/cart?customerId=" + customerId;
    }
}
