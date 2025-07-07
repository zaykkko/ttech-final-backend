package com.techlab.api.repositories;

import com.techlab.api.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameIgnoreCase(String name);
    Boolean existsByNameIgnoreCase(String name);
}
