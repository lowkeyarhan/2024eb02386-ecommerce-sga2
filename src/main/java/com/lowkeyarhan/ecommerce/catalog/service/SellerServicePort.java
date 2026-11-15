package com.lowkeyarhan.ecommerce.catalog.service;

import com.lowkeyarhan.ecommerce.catalog.dto.SellerForm;
import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import java.util.List;

public interface SellerServicePort { List<Seller> findAll(); Seller get(Long id); SellerForm form(Long id); Seller save(SellerForm form); void delete(Long id); }
