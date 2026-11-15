package com.lowkeyarhan.ecommerce.inventory.service;

import com.lowkeyarhan.ecommerce.catalog.repository.ProductRepository;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import com.lowkeyarhan.ecommerce.common.validation.BusinessRules;
import com.lowkeyarhan.ecommerce.inventory.dto.RestockForm;
import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;
import com.lowkeyarhan.ecommerce.inventory.repository.InventoryMovementRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class InventoryService implements InventoryServicePort {
    private final ProductRepository products; private final InventoryMovementRepository movements; private final BusinessRules rules;
    @Transactional public void restock(RestockForm form) {
        rules.requirePositiveQuantity(form.getQuantity());
        var product = products.findByIdForUpdate(form.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        product.setStockQuantity(product.getStockQuantity() + form.getQuantity()); products.save(product);
        InventoryMovement movement = new InventoryMovement(); movement.setProduct(product); movement.setQuantityDelta(form.getQuantity());
        movement.setReason("RESTOCK"); movement.setNote(form.getNote() == null ? "Manual restock" : form.getNote()); movements.save(movement);
    }
    @Transactional(readOnly=true) public List<InventoryMovement> recentMovements() { return movements.findTop20ByOrderByOccurredAtDesc(); }
}
