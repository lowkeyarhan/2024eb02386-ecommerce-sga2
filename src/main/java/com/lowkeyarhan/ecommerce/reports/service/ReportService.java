package com.lowkeyarhan.ecommerce.reports.service;

import com.lowkeyarhan.ecommerce.cart.repository.CartRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.CategoryRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.catalog.repository.SellerRepository;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerRepository;
import com.lowkeyarhan.ecommerce.orders.model.ProductReturn;
import com.lowkeyarhan.ecommerce.orders.model.PurchaseOrder;
import com.lowkeyarhan.ecommerce.orders.repository.OrderRepository;
import com.lowkeyarhan.ecommerce.orders.repository.ProductReturnRepository;
import com.lowkeyarhan.ecommerce.reports.dto.DashboardData;
import com.lowkeyarhan.ecommerce.reports.repository.ReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional(readOnly=true)
public class ReportService implements ReportServicePort {
    private final ProductRepository products; private final CustomerRepository customers; private final SellerRepository sellers;
    private final CartRepository carts; private final OrderRepository orders; private final CategoryRepository categories;
    private final ProductReturnRepository returns; private final ReportRepository reportRepository;
    public DashboardData dashboard() {
        return DashboardData.builder().products(products.count()).customers(customers.count()).sellers(sellers.count())
                .openCarts(carts.countByStatus("OPEN")).activeOrders(orders.countByStatus("CONFIRMED") + orders.countByStatus("PROCESSING") + orders.countByStatus("SHIPPED"))
                .lowStockProducts(products.findLowStockProducts().size()).recentOrders(orders.findTop8ByOrderByOrderDateDesc()).build();
    }
    public List<com.lowkeyarhan.ecommerce.catalog.model.Product> lowStock() { return products.findLowStockProducts(); }
    public List<com.lowkeyarhan.ecommerce.catalog.model.Product> topProducts() { return products.findTopSellingProducts(PageRequest.of(0, 10)).getContent(); }
    public List<ReportRepository.MonthlySalesRow> monthlySales() { return reportRepository.monthlySales(); }
    public List<ProductReturn> pendingReturns() { return returns.findByStatusOrderByRequestedAtDesc("REQUESTED"); }
}
