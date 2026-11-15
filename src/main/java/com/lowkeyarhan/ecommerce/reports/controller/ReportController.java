package com.lowkeyarhan.ecommerce.reports.controller;

import com.lowkeyarhan.ecommerce.reports.service.ReportServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor
public class ReportController {
    private final ReportServicePort reports;
    @GetMapping("/reports") public String index(Model model) { model.addAttribute("dashboard", reports.dashboard()); return "reports/index"; }
    @GetMapping("/reports/low-stock") public String lowStock(Model model) { model.addAttribute("products", reports.lowStock()); return "reports/low-stock"; }
    @GetMapping("/reports/monthly-sales") public String monthlySales(Model model) { model.addAttribute("rows", reports.monthlySales()); return "reports/monthly-sales"; }
    @GetMapping("/reports/top-products") public String topProducts(Model model) { model.addAttribute("products", reports.topProducts()); return "reports/top-products"; }
}
