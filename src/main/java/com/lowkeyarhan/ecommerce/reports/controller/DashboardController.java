package com.lowkeyarhan.ecommerce.reports.controller;

import com.lowkeyarhan.ecommerce.reports.service.ReportServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller @RequiredArgsConstructor
public class DashboardController {
    private final ReportServicePort reports;
    @GetMapping("/") public String dashboard(Model model) { model.addAttribute("dashboard", reports.dashboard()); return "dashboard"; }
}
