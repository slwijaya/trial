//v2
package com.ecommerce.productapi.service;

import com.ecommerce.productapi.model.Product;
import com.ecommerce.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate; // Tambahkan RedisTemplate untuk akses Redis

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);

        // Hapus cache produk dari Redis setelah penghapusan produk dari database
        String cacheKey = "product_" + id;
        redisTemplate.delete(cacheKey); // Redis: Hapus cache terkait produk yang dihapus
    }

    // Implementasi Redis Cache pada method getProductById
    public Product getProductById(Integer id) {
        String cacheKey = "product_" + id;

        // Cek apakah produk ada di Redis (Cache Hit)
        Product cachedProduct = (Product) redisTemplate.opsForValue().get(cacheKey);
        if (cachedProduct != null) {
            System.out.println("Cache Hit: Mengambil produk dari Redis");
            return cachedProduct;
        }

        // Cache Miss, ambil produk dari database
        System.out.println("Cache Miss: Mengambil produk dari database");
        Optional<Product> productOpt = productRepository.findById(id);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            System.out.println("Produk ditemukan di database: " + product.getName());

            // Log tambahan sebelum penyimpanan ke Redis
            System.out.println("Simpan ke Redis dengan key: " + cacheKey + " dan produk: " + product.getName());

            // Simpan produk ke Redis dengan waktu kedaluwarsa
            redisTemplate.opsForValue().set(cacheKey, product, 1, TimeUnit.HOURS);
            System.out.println("Produk disimpan di Redis dengan key: " + cacheKey);

            return product;
        } else {
            System.out.println("Produk dengan id " + id + " tidak ditemukan di database");
            throw new RuntimeException("Product not found");
        }
    }
}


