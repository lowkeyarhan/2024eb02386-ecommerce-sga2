package com.lowkeyarhan.ecommerce.catalog.controller;

import com.lowkeyarhan.ecommerce.catalog.dto.CategoryForm;
import com.lowkeyarhan.ecommerce.catalog.service.CategoryServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class CategoryController {
    private final CategoryServicePort categories;
    @GetMapping("/categories") public String list(Model model) { model.addAttribute("categories", categories.findAll()); return "categories/list"; }
    @GetMapping("/categories/new") public String create(Model model) { model.addAttribute("form", new CategoryForm()); model.addAttribute("allCategories", categories.findAll()); return "categories/form"; }
    @GetMapping("/categories/{id}/edit") public String edit(@PathVariable Long id, Model model) { model.addAttribute("form", categories.form(id)); model.addAttribute("allCategories", categories.findAll()); return "categories/form"; }
    @PostMapping("/categories/save") public String save(@Valid @ModelAttribute("form") CategoryForm form, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) { model.addAttribute("allCategories", categories.findAll()); return "categories/form"; }
        categories.save(form); flash.addFlashAttribute("success", "Category saved."); return "redirect:/categories";
    }
    @PostMapping("/categories/{id}/delete") public String delete(@PathVariable Long id, RedirectAttributes flash) { categories.delete(id); flash.addFlashAttribute("success", "Category deleted."); return "redirect:/categories"; }
}
