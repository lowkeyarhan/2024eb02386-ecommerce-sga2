package com.lowkeyarhan.ecommerce.catalog.repository;

import com.lowkeyarhan.ecommerce.catalog.model.Product;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths={"category", "seller"})
    @Query("select p from Product p join p.category c join p.seller s where (:keyword is null or :keyword = '' or lower(p.productName) like lower(concat('%', :keyword, '%')) or lower(p.sku) like lower(concat('%', :keyword, '%'))) and (:categoryId is null or c.id = :categoryId) and (:sellerId is null or s.id = :sellerId) and (:activeOnly = false or p.active = true)")
    Page<Product> searchCatalogue(@Param("keyword") String keyword, @Param("categoryId") Long categoryId,
                                  @Param("sellerId") Long sellerId, @Param("activeOnly") boolean activeOnly, Pageable pageable);

    @EntityGraph(attributePaths={"seller", "category"})
    @Query("select p from Product p where p.stockQuantity <= p.reorderLevel order by p.stockQuantity asc, p.productName asc")
    List<Product> findLowStockProducts();

    @EntityGraph(attributePaths={"seller", "category"})
    @Query("select p from Product p join p.orderItems oi join oi.order o where o.status <> 'CANCELLED' group by p.id order by sum(oi.quantity) desc")
    Page<Product> findTopSellingProducts(Pageable pageable);

    @EntityGraph(attributePaths={"category", "seller"})
    Optional<Product> findDetailedById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.id = :id")
    Optional<Product> findByIdForUpdate(@Param("id") Long id);
}
