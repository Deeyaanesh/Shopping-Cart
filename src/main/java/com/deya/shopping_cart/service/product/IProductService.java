package com.deya.shopping_cart.service.product;

import com.deya.shopping_cart.DTO.ProductDto;
import com.deya.shopping_cart.model.Product;
import com.deya.shopping_cart.request.AddProductRequest;
import com.deya.shopping_cart.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    void deleteProduct(Long id);

    Product updateProduct(UpdateProductRequest product, Long id);

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByBrandAndName(String category, String name);

    Long countProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConverted(List<Product> products);

    ProductDto convertProductToDto(Product product);

    ProductDto convertProductToDto(Long productId);
}
