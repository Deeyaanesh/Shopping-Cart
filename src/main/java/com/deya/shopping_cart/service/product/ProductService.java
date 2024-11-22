package com.deya.shopping_cart.service.product;

import com.deya.shopping_cart.DTO.ImageDto;
import com.deya.shopping_cart.DTO.ProductDto;
import com.deya.shopping_cart.exceptions.AlreadyExistsException;
import com.deya.shopping_cart.exceptions.ProductNotFoundException;
import com.deya.shopping_cart.exceptions.ResourceNotFoundException;
import com.deya.shopping_cart.model.Category;
import com.deya.shopping_cart.model.Image;
import com.deya.shopping_cart.model.Product;
import com.deya.shopping_cart.repository.CategoryRepository;
import com.deya.shopping_cart.repository.ImageRepository;
import com.deya.shopping_cart.repository.ProductRepository;
import com.deya.shopping_cart.request.AddProductRequest;
import com.deya.shopping_cart.request.UpdateProductRequest;
import com.deya.shopping_cart.service.category.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Product addProduct(AddProductRequest request) {

        //Check if the category is present in the database or not.
        //If the category exist, then find the respective category from the category table in DB
        //Else
        //  1. create a new category
        //  2. save the category in the category table in the DB
        //Reset the type of category of the newly created duplicate product
        //Finally save the newly created product to the product repository

        if(isProductExist(request.getName(), request.getBrand())){
            throw new AlreadyExistsException(request.getName() + " with " + request.getBrand() + " already exists");
        }

        Category category = Optional.ofNullable(categoryRepository
                .findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean isProductExist(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getQuantity(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(exisitingProduct -> updateExistingProduct(exisitingProduct, request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setQuantity(request.getQuantity());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found!"));
    }

    @Override
    public void deleteProduct(Long id) {
//        Product product = getProductById(id);
//        if(product != null){
//            Category category = categoryService.getCategoryById(product.getCategory().getId());
//            categoryRepository
//        }
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                () -> {throw new ProductNotFoundException("Product Not found!");
                });
    }



    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConverted(List<Product> products){
        return products.stream().map(this::convertProductToDto).toList();
    }

    @Override
    public ProductDto convertProductToDto(Product product){
        ProductDto productDto = modelMapper.map(product , ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtoList = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtoList);
        return productDto;
    }

    @Override
    public ProductDto convertProductToDto(Long productId){
        Product product = getProductById(productId);
        ProductDto productDto = modelMapper.map(product , ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtoList = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtoList);
        return productDto;
    }

}
