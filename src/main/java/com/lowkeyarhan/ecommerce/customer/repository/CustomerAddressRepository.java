package com.lowkeyarhan.ecommerce.customer.repository;

import com.lowkeyarhan.ecommerce.customer.model.CustomerAddress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByCustomer_IdOrderByDefaultAddressDescIdAsc(Long customerId);
}
