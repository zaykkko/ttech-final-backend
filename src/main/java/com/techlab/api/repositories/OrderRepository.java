package com.techlab.api.repositories;

import com.techlab.api.entities.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "orderProducts.product")
    @Query("SELECT o FROM Order o")
    List<Order> findAllWithItemsByCreatedAtDesc();

    @EntityGraph(attributePaths = "orderProducts.product")
    Optional<Order> findOrderWithProductsById(Long id);
}
