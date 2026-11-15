package com.lowkeyarhan.ecommerce.catalog.service;

import com.lowkeyarhan.ecommerce.catalog.dto.SellerForm;
import com.lowkeyarhan.ecommerce.catalog.mapper.SellerMapper;
import com.lowkeyarhan.ecommerce.catalog.model.Seller;
import com.lowkeyarhan.ecommerce.catalog.repository.SellerRepository;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional(readOnly=true)
public class SellerService implements SellerServicePort {
    private final SellerRepository repository; private final SellerMapper mapper;
    public List<Seller> findAll() { return repository.findAllByOrderBySellerNameAsc(); }
    public SellerForm form(Long id) { return mapper.toForm(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + id))); }
    public Seller get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + id)); }
    @Transactional public Seller save(SellerForm form) {
        Seller seller = form.getId() == null ? new Seller() : get(form.getId());
        seller.setSellerName(form.getSellerName().trim()); seller.setContactEmail(form.getContactEmail().trim().toLowerCase());
        seller.setStatus(form.getStatus() == null ? "ACTIVE" : form.getStatus()); return repository.save(seller);
    }
    @Transactional public void delete(Long id) { repository.delete(get(id)); }
}
