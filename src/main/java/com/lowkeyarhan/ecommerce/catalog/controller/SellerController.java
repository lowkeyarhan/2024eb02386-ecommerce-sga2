package com.lowkeyarhan.ecommerce.catalog.controller;

import com.lowkeyarhan.ecommerce.catalog.dto.SellerForm;
import com.lowkeyarhan.ecommerce.catalog.service.SellerServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class SellerController {
    private final SellerServicePort sellers;
    @GetMapping("/sellers") public String list(Model model) { model.addAttribute("sellers", sellers.findAll()); return "sellers/list"; }
    @GetMapping("/sellers/new") public String create(Model model) { model.addAttribute("form", new SellerForm()); return "sellers/form"; }
    @GetMapping("/sellers/{id}/edit") public String edit(@PathVariable Long id, Model model) { model.addAttribute("form", sellers.form(id)); return "sellers/form"; }
    @PostMapping("/sellers/save") public String save(@Valid @ModelAttribute("form") SellerForm form, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) return "sellers/form"; sellers.save(form); flash.addFlashAttribute("success", "Seller saved."); return "redirect:/sellers";
    }
    @PostMapping("/sellers/{id}/delete") public String delete(@PathVariable Long id, RedirectAttributes flash) { sellers.delete(id); flash.addFlashAttribute("success", "Seller deleted."); return "redirect:/sellers"; }
}
