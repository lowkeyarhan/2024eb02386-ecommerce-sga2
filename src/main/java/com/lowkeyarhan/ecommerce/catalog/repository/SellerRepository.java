package com.lowkeyarhan.ecommerce.catalog.repository;

import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    List<Seller> findAllByOrderBySellerNameAsc();
}
