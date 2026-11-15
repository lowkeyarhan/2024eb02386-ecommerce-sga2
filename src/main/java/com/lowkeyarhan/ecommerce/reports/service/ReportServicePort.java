package com.lowkeyarhan.ecommerce.reports.service;

import com.lowkeyarhan.ecommerce.catalog.model.Product;
import com.lowkeyarhan.ecommerce.orders.model.ProductReturn;
import com.lowkeyarhan.ecommerce.reports.dto.DashboardData;
import com.lowkeyarhan.ecommerce.reports.repository.ReportRepository;
import java.util.List;

public interface ReportServicePort { DashboardData dashboard(); List<Product> lowStock(); List<Product> topProducts(); List<ReportRepository.MonthlySalesRow> monthlySales(); List<ProductReturn> pendingReturns(); }
