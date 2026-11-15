package com.lowkeyarhan.ecommerce.customer.repository;

import com.lowkeyarhan.ecommerce.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c from Customer c where (:keyword is null or :keyword = '' or lower(concat(c.firstName, ' ', c.lastName)) like lower(concat('%', :keyword, '%')) or lower(c.email) like lower(concat('%', :keyword, '%')))")
    Page<Customer> searchCustomers(@Param("keyword") String keyword, Pageable pageable);
}
