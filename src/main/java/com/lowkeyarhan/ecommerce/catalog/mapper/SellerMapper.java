package com.lowkeyarhan.ecommerce.catalog.mapper;

import com.lowkeyarhan.ecommerce.catalog.dto.SellerForm;
import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {
    public SellerForm toForm(Seller seller) {
        SellerForm form = new SellerForm(); form.setId(seller.getId()); form.setSellerName(seller.getSellerName()); form.setContactEmail(seller.getContactEmail()); form.setStatus(seller.getStatus()); return form;
    }
}
