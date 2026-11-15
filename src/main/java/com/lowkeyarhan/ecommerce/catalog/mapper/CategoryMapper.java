package com.lowkeyarhan.ecommerce.catalog.mapper;

import com.lowkeyarhan.ecommerce.catalog.dto.CategoryForm;
import com.lowkeyarhan.ecommerce.catalog.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryForm toForm(Category category) {
        CategoryForm form = new CategoryForm(); form.setId(category.getId()); form.setCategoryName(category.getCategoryName());
        form.setParentCategoryId(category.getParentCategory() == null ? null : category.getParentCategory().getId()); return form;
    }
}
