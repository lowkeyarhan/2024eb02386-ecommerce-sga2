package com.lowkeyarhan.ecommerce.orders.controller;

import com.lowkeyarhan.ecommerce.orders.dto.ReturnRequestForm;
import com.lowkeyarhan.ecommerce.orders.service.OrderServicePort;
import com.lowkeyarhan.ecommerce.orders.service.ReturnServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequiredArgsConstructor
public class ReturnController {
    private final ReturnServicePort returns; private final OrderServicePort orders;
    @GetMapping("/returns") public String list(Model model) { model.addAttribute("returns", returns.findAll()); return "returns/list"; }
    @GetMapping("/returns/new") public String form(@RequestParam Long orderId, Model model) {
        model.addAttribute("form", new ReturnRequestForm()); model.addAttribute("order", orders.get(orderId)); model.addAttribute("items", orders.items(orderId)); return "returns/form";
    }
    @PostMapping("/returns") public String request(@Valid @ModelAttribute("form") ReturnRequestForm form, BindingResult result, RedirectAttributes flash, Model model) {
        if (result.hasErrors()) { flash.addFlashAttribute("error", "Return details are incomplete."); return "redirect:/orders/" + form.getOrderId(); }
        returns.request(form); flash.addFlashAttribute("success", "Return request submitted for review."); return "redirect:/returns";
    }
    @PostMapping("/returns/{id}/action") public String action(@PathVariable Long id, @RequestParam String action, RedirectAttributes flash) {
        returns.process(id, action); flash.addFlashAttribute("success", "Return workflow updated."); return "redirect:/returns";
    }
}
