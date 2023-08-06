package com.jacidizadkiel.javazadkieljacidi.services;

import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;
import com.jacidizadkiel.javazadkieljacidi.models.Product;
import com.jacidizadkiel.javazadkieljacidi.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product updatedProduct) {
        String id = updatedProduct.getId();
        Product existingProduct = getProductById(id);
        
        if (existingProduct == null) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCost(updatedProduct.getCost());
        existingProduct.setMinPrio(updatedProduct.getMinPrio());

        return productRepository.save(existingProduct);
    }

    public void deleteProductById(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            productRepository.delete(product);
        } else {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
        }
    }
}