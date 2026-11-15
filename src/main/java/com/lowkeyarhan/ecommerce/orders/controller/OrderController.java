package com.lowkeyarhan.ecommerce.orders.controller;

import com.lowkeyarhan.ecommerce.orders.dto.CheckoutForm;
import com.lowkeyarhan.ecommerce.orders.service.OrderServicePort;
import com.lowkeyarhan.ecommerce.customer.service.CustomerServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class OrderController {
    private final OrderServicePort orders; private final CustomerServicePort customers;
    @GetMapping("/orders") public String list(@RequestParam(required=false) String status, @RequestParam(required=false) String q,
            @RequestParam(defaultValue="0") int page, Model model) {
        var result = orders.search(status, q, PageRequest.of(Math.max(page,0), 10, Sort.by(Sort.Direction.DESC,"orderDate")));
        model.addAttribute("orderPage", result); model.addAttribute("orders", result.getContent()); model.addAttribute("status", status); model.addAttribute("q", q); return "orders/list";
    }
    @GetMapping("/orders/history") public String history(@RequestParam(defaultValue="1") Long customerId, @RequestParam(defaultValue="0") int page, Model model) {
        var result = orders.history(customerId, PageRequest.of(Math.max(page,0), 10)); model.addAttribute("orderPage", result);
        model.addAttribute("orders", result.getContent()); model.addAttribute("customers", customers.findAll()); model.addAttribute("selectedCustomerId", customerId); return "orders/history";
    }
    @GetMapping("/orders/{id}") public String detail(@PathVariable Long id, Model model) {
        var order = orders.get(id); model.addAttribute("order", order); model.addAttribute("items", orders.items(id));
        model.addAttribute("payments", orders.payments(id)); model.addAttribute("shipment", orders.shipment(id).orElse(null)); return "orders/detail";
    }
    @PostMapping("/orders/checkout") public String checkout(@RequestParam(defaultValue="1") Long customerId, @Valid @ModelAttribute("form") CheckoutForm form,
            BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) { flash.addFlashAttribute("error", "Choose a shipping address before checking out."); return "redirect:/cart?customerId=" + customerId; }
        var order = orders.checkout(customerId, form.getShippingAddressId()); flash.addFlashAttribute("success", "Order placed. Stock and order records were committed together.");
        return "redirect:/orders/" + order.getId();
    }
}
