package com.lowkeyarhan.ecommerce.reports.dto;

import java.util.List;
import com.lowkeyarhan.ecommerce.orders.model.PurchaseOrder;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class DashboardData {
    private long products; private long customers; private long sellers; private long openCarts; private long activeOrders; private long lowStockProducts;
    private List<PurchaseOrder> recentOrders;
}
