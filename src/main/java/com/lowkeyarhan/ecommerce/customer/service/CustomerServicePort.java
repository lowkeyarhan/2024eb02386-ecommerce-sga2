package com.lowkeyarhan.ecommerce.customer.service;

import com.lowkeyarhan.ecommerce.customer.dto.CustomerForm;
import com.lowkeyarhan.ecommerce.customer.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomerServicePort { Page<Customer> search(String q, Pageable pageable); List<Customer> findAll(); Customer get(Long id); CustomerForm form(Long id); Customer save(CustomerForm form); void delete(Long id); }
