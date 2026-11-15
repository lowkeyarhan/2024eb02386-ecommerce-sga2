package com.lowkeyarhan.ecommerce.orders.repository;

import com.lowkeyarhan.ecommerce.orders.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("select oi from OrderItem oi join fetch oi.product where oi.order.id = :orderId order by oi.id")
    List<OrderItem> findDetailedItems(@Param("orderId") Long orderId);
    List<OrderItem> findByOrder_Id(Long orderId);
}
