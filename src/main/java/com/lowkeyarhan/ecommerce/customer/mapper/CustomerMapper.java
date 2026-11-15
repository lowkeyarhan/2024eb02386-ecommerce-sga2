package com.lowkeyarhan.ecommerce.customer.mapper;

import com.lowkeyarhan.ecommerce.customer.dto.CustomerForm;
import com.lowkeyarhan.ecommerce.customer.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public CustomerForm toForm(Customer customer) {
        CustomerForm form = new CustomerForm(); form.setId(customer.getId()); form.setFirstName(customer.getFirstName());
        form.setLastName(customer.getLastName()); form.setEmail(customer.getEmail()); form.setPhone(customer.getPhone()); return form;
    }
}
