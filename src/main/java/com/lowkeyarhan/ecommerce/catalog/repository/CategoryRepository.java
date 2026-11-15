package com.lowkeyarhan.ecommerce.catalog.repository;

import com.lowkeyarhan.ecommerce.catalog.model.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @EntityGraph(attributePaths={"parentCategory"})
    List<Category> findAllByOrderByCategoryNameAsc();
}
