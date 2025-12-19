package de.onlineshop.onlineshop_backend.controller;

import de.onlineshop.onlineshop_backend.model.Product;
import de.onlineshop.onlineshop_backend.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//Frontent → Backend Aufrufe erlauben.
@CrossOrigin(origins = "http://localhost:5173")  // <--- NEU
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    // Constructor Injection
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // GET /api/products -> alle Produkte
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET /api/products/{id} -> einzelnes Produkt
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)               // 200 OK + Produkt
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404, wenn nicht gefunden
    }
}
