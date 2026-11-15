package com.lowkeyarhan.ecommerce.orders.service;

import com.lowkeyarhan.ecommerce.orders.dto.ReturnRequestForm;
import com.lowkeyarhan.ecommerce.orders.model.ProductReturn;
import java.util.List;

public interface ReturnServicePort { ProductReturn request(ReturnRequestForm form); ProductReturn process(Long returnId, String action); List<ProductReturn> findAll(); }
