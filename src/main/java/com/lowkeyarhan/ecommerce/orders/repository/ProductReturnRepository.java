package com.lowkeyarhan.ecommerce.orders.repository;

import com.lowkeyarhan.ecommerce.orders.model.ProductReturn;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ProductReturnRepository extends JpaRepository<ProductReturn, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from ProductReturn r where r.id = :id")
    java.util.Optional<ProductReturn> findByIdForUpdate(@Param("id") Long id);
    @EntityGraph(attributePaths={"order", "order.customer"})
    List<ProductReturn> findByStatusOrderByRequestedAtDesc(String status);
    @EntityGraph(attributePaths={"order", "order.customer"})
    List<ProductReturn> findAllByOrderByRequestedAtDesc();
}
