package com.techlab.api.repositories;

import com.techlab.api.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = {"productCategories.category"})
    @Query("SELECT p FROM Product p")
    List<Product> findAllWithCategories();

    @EntityGraph(attributePaths = {"productCategories.category"})
    Optional<Product> findWithCategoriesById(Long id);

    @EntityGraph(attributePaths = {"productCategories.category"})
    List<Product> findByNameContainingIgnoreCase(String name);

    Boolean existsByNameIgnoreCase(String name);
}
