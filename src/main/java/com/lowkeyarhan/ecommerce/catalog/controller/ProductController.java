package com.lowkeyarhan.ecommerce.catalog.controller;

import com.lowkeyarhan.ecommerce.catalog.dto.ProductForm;
import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.catalog.repository.CategoryRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.SellerRepository;
import com.lowkeyarhan.ecommerce.catalog.service.ProductServicePort;
import com.lowkeyarhan.ecommerce.customer.service.CustomerServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class ProductController {
    private final ProductServicePort products; private final CategoryRepository categories; private final SellerRepository sellers;
    private final CustomerServicePort customers;

    @GetMapping("/products") public String list(@RequestParam(required=false) String q, @RequestParam(required=false) Long categoryId,
            @RequestParam(required=false) Long sellerId, @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="productName") String sort,
            @RequestParam(defaultValue="asc") String direction, @RequestParam(defaultValue="1") Long customerId, Model model) {
        String safeSort = java.util.Set.of("productName","unitPrice","stockQuantity","createdAt","sku").contains(sort) ? sort : "productName";
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(Math.max(page,0), Math.min(Math.max(size,5),30), Sort.by(dir, safeSort));
        Page<Product> result = products.search(q, categoryId, sellerId, false, pageable);
        model.addAttribute("productPage", result); model.addAttribute("products", result.getContent());
        model.addAttribute("categories", categories.findAllByOrderByCategoryNameAsc()); model.addAttribute("sellers", sellers.findAllByOrderBySellerNameAsc());
        model.addAttribute("q", q); model.addAttribute("categoryId", categoryId); model.addAttribute("sellerId", sellerId);
        model.addAttribute("sort", safeSort); model.addAttribute("direction", dir.name().toLowerCase());
        model.addAttribute("customers", customers.findAll()); model.addAttribute("selectedCustomerId", customerId); return "products/list";
    }
    @GetMapping("/products/new") public String createForm(Model model) { model.addAttribute("form", new ProductForm()); addLookups(model); return "products/form"; }
    @GetMapping("/products/{id}/edit") public String editForm(@PathVariable Long id, Model model) { model.addAttribute("form", products.form(id)); addLookups(model); return "products/form"; }
    @PostMapping("/products/save") public String save(@Valid @ModelAttribute("form") ProductForm form, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) { addLookups(model); return "products/form"; }
        products.save(form); flash.addFlashAttribute("success", "Product saved successfully."); return "redirect:/products";
    }
    @PostMapping("/products/{id}/delete") public String delete(@PathVariable Long id, RedirectAttributes flash) { products.delete(id); flash.addFlashAttribute("success", "Product removed."); return "redirect:/products"; }
    @GetMapping("/api/products/{id}/stock") @ResponseBody public java.util.Map<String,Object> stock(@PathVariable Long id) {
        Product product = products.get(id); return java.util.Map.of("productId", id, "productName", product.getProductName(), "stock", product.getStockQuantity(), "active", product.isActive());
    }
    private void addLookups(Model model) { model.addAttribute("categories", categories.findAllByOrderByCategoryNameAsc()); model.addAttribute("sellers", sellers.findAllByOrderBySellerNameAsc()); }
}
