package com.techlab.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 60, unique = true)
    private String name;

    public Category() {}

    public Category(String name) {
        this.setName(name);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public static class CategoryBuilder {
        private String name;

        public CategoryBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Category build() {
            return new Category(this.name);
        }
    }

    public static class OrderItem {
    }
}
