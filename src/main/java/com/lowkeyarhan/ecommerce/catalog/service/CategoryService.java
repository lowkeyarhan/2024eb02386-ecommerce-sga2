package com.lowkeyarhan.ecommerce.catalog.service;

import com.lowkeyarhan.ecommerce.catalog.dto.CategoryForm;
import com.lowkeyarhan.ecommerce.catalog.mapper.CategoryMapper;
import com.lowkeyarhan.ecommerce.catalog.model.Category;
import com.lowkeyarhan.ecommerce.catalog.repository.CategoryRepository;
import com.lowkeyarhan.ecommerce.common.error.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional(readOnly=true)
public class CategoryService implements CategoryServicePort {
    private final CategoryRepository repository; private final CategoryMapper mapper;
    public List<Category> findAll() { return repository.findAllByOrderByCategoryNameAsc(); }
    public Category get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id)); }
    public CategoryForm form(Long id) { return mapper.toForm(get(id)); }
    @Transactional public Category save(CategoryForm form) {
        Category category = form.getId() == null ? new Category() : get(form.getId());
        category.setCategoryName(form.getCategoryName().trim());
        category.setParentCategory(form.getParentCategoryId() == null ? null : get(form.getParentCategoryId()));
        if (category.getId() != null && category.getParentCategory() != null && category.getId().equals(category.getParentCategory().getId())) throw new IllegalArgumentException("A category cannot be its own parent.");
        return repository.save(category);
    }
    @Transactional public void delete(Long id) { repository.delete(get(id)); }
}
