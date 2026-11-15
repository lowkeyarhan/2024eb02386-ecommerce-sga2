package com.lowkeyarhan.ecommerce.orders.repository;

import com.lowkeyarhan.ecommerce.orders.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> { java.util.Optional<Shipment> findByOrder_Id(Long orderId); }
