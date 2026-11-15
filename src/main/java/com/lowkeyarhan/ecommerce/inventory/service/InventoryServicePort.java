package com.lowkeyarhan.ecommerce.inventory.service;

import com.lowkeyarhan.ecommerce.inventory.dto.RestockForm;
import com.lowkeyarhan.ecommerce.inventory.model.InventoryMovement;
import java.util.List;

public interface InventoryServicePort { void restock(RestockForm form); List<InventoryMovement> recentMovements(); }
