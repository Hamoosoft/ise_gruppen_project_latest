package de.onlineshop.onlineshop_backend.repository;


import de.onlineshop.onlineshop_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Standard CRUD-Methoden sind schon da (findAll, findById, save, delete, ...)
}
