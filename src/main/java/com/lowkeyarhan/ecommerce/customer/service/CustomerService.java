package com.lowkeyarhan.ecommerce.customer.service;

import com.lowkeyarhan.ecommerce.customer.dto.CustomerForm;
import com.lowkeyarhan.ecommerce.customer.mapper.CustomerMapper;
import com.lowkeyarhan.ecommerce.customer.model.Customer;
import com.lowkeyarhan.ecommerce.customer.repository.CustomerRepository;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional(readOnly=true)
public class CustomerService implements CustomerServicePort {
    private final CustomerRepository repository; private final CustomerMapper mapper;
    public Page<Customer> search(String q, Pageable pageable) { return repository.searchCustomers(q == null || q.isBlank() ? null : q.trim().toLowerCase(Locale.ROOT), pageable); }
    public java.util.List<Customer> findAll() { return repository.findAll(org.springframework.data.domain.Sort.by("firstName").ascending()); }
    public Customer get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id)); }
    public CustomerForm form(Long id) { return mapper.toForm(get(id)); }
    @Transactional public Customer save(CustomerForm form) {
        Customer c = form.getId() == null ? new Customer() : get(form.getId());
        c.setFirstName(form.getFirstName().trim()); c.setLastName(form.getLastName().trim()); c.setEmail(form.getEmail().trim().toLowerCase()); c.setPhone(form.getPhone()); return repository.save(c);
    }
    @Transactional public void delete(Long id) { repository.delete(get(id)); }
}
