package com.lowkeyarhan.ecommerce.inventory.controller;

import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.inventory.dto.RestockForm;
import com.lowkeyarhan.ecommerce.inventory.service.InventoryServicePort;
import com.lowkeyarhan.ecommerce.reports.service.ReportServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class InventoryController {
    private final InventoryServicePort inventory; private final ReportServicePort reports; private final ProductRepository products;
    @GetMapping("/inventory") public String index(Model model) {
        model.addAttribute("lowStock", reports.lowStock()); model.addAttribute("movements", inventory.recentMovements());
        model.addAttribute("products", products.findAll(org.springframework.data.domain.Sort.by("productName")));
        model.addAttribute("form", new RestockForm()); return "inventory/index";
    }
    @PostMapping("/inventory/restock") public String restock(@Valid @ModelAttribute("form") RestockForm form, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) { flash.addFlashAttribute("error", "Select a product and enter a positive restock quantity."); return "redirect:/inventory"; }
        inventory.restock(form); flash.addFlashAttribute("success", "Stock replenished and movement ledger updated."); return "redirect:/inventory";
    }
}
