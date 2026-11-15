package com.lowkeyarhan.ecommerce.inventory.repository;

import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    @EntityGraph(attributePaths={"product"})
    List<InventoryMovement> findTop20ByOrderByOccurredAtDesc();
    List<InventoryMovement> findByProduct_IdOrderByOccurredAtDesc(Long productId);
}
