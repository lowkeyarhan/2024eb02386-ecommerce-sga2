package com.lowkeyarhan.ecommerce.customer.controller;

import com.lowkeyarhan.ecommerce.customer.dto.CustomerForm;
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
public class CustomerController {
    private final CustomerServicePort customers;
    @GetMapping("/customers") public String list(@RequestParam(required=false) String q, @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="firstName") String sort, @RequestParam(defaultValue="asc") String direction, Model model) {
        String safeSort = java.util.Set.of("firstName", "lastName", "email", "createdAt").contains(sort) ? sort : "firstName";
        Sort.Direction order = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var result = customers.search(q, PageRequest.of(Math.max(page,0), 10, Sort.by(order, safeSort)));
        model.addAttribute("customerPage", result); model.addAttribute("customers", result.getContent());
        model.addAttribute("q", q); model.addAttribute("sort", safeSort); model.addAttribute("direction", order.name().toLowerCase()); return "customers/list";
    }
    @GetMapping("/customers/new") public String create(Model model) { model.addAttribute("form", new CustomerForm()); return "customers/form"; }
    @GetMapping("/customers/{id}/edit") public String edit(@PathVariable Long id, Model model) { model.addAttribute("form", customers.form(id)); return "customers/form"; }
    @PostMapping("/customers/save") public String save(@Valid @ModelAttribute("form") CustomerForm form, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) return "customers/form"; customers.save(form); flash.addFlashAttribute("success", "Customer saved."); return "redirect:/customers";
    }
    @PostMapping("/customers/{id}/delete") public String delete(@PathVariable Long id, RedirectAttributes flash) { customers.delete(id); flash.addFlashAttribute("success", "Customer deleted."); return "redirect:/customers"; }
}
