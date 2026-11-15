package com.lowkeyarhan.ecommerce.orders.repository;

import com.lowkeyarhan.ecommerce.orders.model.ReturnItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReturnItemRepository extends JpaRepository<ReturnItem, Long> {
    List<ReturnItem> findByProductReturn_Id(Long returnId);
    @Query("select coalesce(sum(ri.quantity), 0) from ReturnItem ri where ri.orderItem.id = :orderItemId and ri.productReturn.status in ('REQUESTED','APPROVED','RECEIVED')")
    Long sumReservedReturnQuantity(@Param("orderItemId") Long orderItemId);
    @Query("select coalesce(sum(ri.quantity), 0) from ReturnItem ri where ri.orderItem.id = :orderItemId and ri.productReturn.status = 'RECEIVED'")
    Long sumReceivedReturnQuantity(@Param("orderItemId") Long orderItemId);
}
