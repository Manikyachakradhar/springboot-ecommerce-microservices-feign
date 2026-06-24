package com.Ecommerce.ProductService.service;


import com.Ecommerce.ProductService.Exception.ProductNotFoundException;
import com.Ecommerce.ProductService.entity.Product;
import com.Ecommerce.ProductService.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private static final Logger log =
            LoggerFactory.getLogger(ProductService.class);


    public Product addProduct(Product product){
        log.info("addProduct() called - Adding product: {}", product.getName());
        return repository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable){

        log.info("Fetching products with pagination: {}", pageable);
        return repository.findAll(pageable);
    }

    public Product getProductById(Long id){
        log.info("getProductById() called - productId: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product not found for id: {}", id);
                    return new ProductNotFoundException("Product not found");
                });
    }

    public void deleteProduct(Long id){
        log.info("deleteProduct() called - productId: {}", id);
    try {
       repository.findById(id)
                .orElseThrow(() -> {
                    return new ProductNotFoundException("Product not found");
                });
        repository.deleteById(id);
        log.info("deleteProduct() called - Deleting productId: {}", id);
    }
    catch (Exception e){
        log.error("deleteProduct() failed for productId: {}",e.getMessage());
        throw (e);
    }

    }

    @Transactional
    public void reduceStock(Long id,
                            Integer quantity){

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found"));

        if(product.getQuantity() < quantity){
            throw new RuntimeException("Insufficient stock");
        }

        product.setQuantity(
                product.getQuantity() - quantity
        );

        repository.save(product);
    }
}
