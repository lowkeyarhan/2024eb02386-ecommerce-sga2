package com.lowkeyarhan.ecommerce.orders.service;

import com.lowkeyarhan.ecommerce.orders.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface OrderServicePort { PurchaseOrder checkout(Long customerId, Long addressId); Page<PurchaseOrder> search(String status, String keyword, Pageable pageable); Page<PurchaseOrder> history(Long customerId, Pageable pageable); PurchaseOrder get(Long id); List<OrderItem> items(Long orderId); List<Payment> payments(Long orderId); Optional<Shipment> shipment(Long orderId); List<PurchaseOrder> recent(); }
