package com.jacidizadkiel.javazadkieljacidi.controllers;
import com.jacidizadkiel.javazadkieljacidi.models.Product;
import com.jacidizadkiel.javazadkieljacidi.services.ProductService;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiErrorResponse;
import com.jacidizadkiel.javazadkieljacidi.dtos.ApiOkResponse;
import com.jacidizadkiel.javazadkieljacidi.exceptions.BadRequestException;
import com.jacidizadkiel.javazadkieljacidi.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Object> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        ApiOkResponse<List<Product>> response = new ApiOkResponse<>("Membership list sent successfully", HttpStatus.OK.value(), products);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {

            Product product = productService.getProductById(id);
            ApiOkResponse<Product> response = new ApiOkResponse<>("Product retrieved successfully", HttpStatus.OK.value(), product);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Product not found", HttpStatus.NOT_FOUND.value(),
                    new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        try {

            Product createdProduct = productService.createProduct(product);
            ApiOkResponse<Product> response = new ApiOkResponse<>("Product created successfully", HttpStatus.CREATED.value(), createdProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable String id, @RequestBody Product request) {
        try {
            Product existingProduct = productService.getProductById(id);
            
            if (existingProduct == null) {
                throw new ResourceNotFoundException("Product not found");
            }

            String newName = request.getName();
            BigDecimal newCost = request.getCost();
            Integer newMinPrio = request.getMinPrio();

            if (newName != null) {
                existingProduct.setName(newName);
            }

            if (newCost != null) {
                existingProduct.setCost(newCost);
            }
       
            if (newMinPrio != null) {
                existingProduct.setMinPrio(newMinPrio);
            }

            Product updatedProduct = productService.updateProduct(existingProduct);
            ApiOkResponse<Product> response = new ApiOkResponse<>("Membership updated successfully", HttpStatus.OK.value(), updatedProduct);

            return ResponseEntity.ok(response);

        } catch (BadRequestException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse("Invalid value", HttpStatus.BAD_REQUEST.value(),
                new ApiErrorResponse.ErrorDetail(e.getMessage(), "BAD_REQUEST", null)));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Membership not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable String id) {
        try {
            productService.deleteProductById(id);
            ApiOkResponse<Object> response = new ApiOkResponse<>("Product deleted successfully", HttpStatus.OK.value(), null);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse("Membership not found", HttpStatus.NOT_FOUND.value(),
                new ApiErrorResponse.ErrorDetail("Resource not found", "NOT_FOUND", null)));

        } catch (Exception e) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new ApiErrorResponse.ErrorDetail("Try again later", "INTERNAL_SERVER_ERROR", null)));
        }
    }
    
}