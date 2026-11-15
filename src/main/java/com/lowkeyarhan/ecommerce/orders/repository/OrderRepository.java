package com.lowkeyarhan.ecommerce.orders.repository;

import com.lowkeyarhan.ecommerce.orders.model.PurchaseOrder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from PurchaseOrder o where o.id = :id")
    java.util.Optional<PurchaseOrder> findByIdForUpdate(@Param("id") Long id);
    @EntityGraph(attributePaths={"customer", "shippingAddress"})
    Page<PurchaseOrder> findByCustomer_IdOrderByOrderDateDesc(Long customerId, Pageable pageable);
    @EntityGraph(attributePaths={"customer"})
    List<PurchaseOrder> findTop8ByOrderByOrderDateDesc();
    long countByStatus(String status);
    @EntityGraph(attributePaths={"customer", "shippingAddress"})
    java.util.Optional<PurchaseOrder> findDetailedById(Long id);
    @EntityGraph(attributePaths={"customer", "shippingAddress"})
    @Query("select o from PurchaseOrder o where (:status is null or :status = '' or o.status = :status) and (:keyword is null or :keyword = '' or lower(concat(o.customer.firstName, ' ', o.customer.lastName)) like lower(concat('%', :keyword, '%')) or cast(o.id as string) like concat('%', :keyword, '%'))")
    Page<PurchaseOrder> searchOrders(@Param("status") String status, @Param("keyword") String keyword, Pageable pageable);
}
