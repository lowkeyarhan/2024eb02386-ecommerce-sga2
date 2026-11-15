package com.lowkeyarhan.ecommerce.catalog.service;

import com.lowkeyarhan.ecommerce.catalog.dto.CategoryForm;
import com.lowkeyarhan.ecommerce.catalog.model.Category;
import java.util.List;

public interface CategoryServicePort { List<Category> findAll(); Category get(Long id); CategoryForm form(Long id); Category save(CategoryForm form); void delete(Long id); }
