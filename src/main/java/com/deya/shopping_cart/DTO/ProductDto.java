package com.deya.shopping_cart.DTO;

import com.deya.shopping_cart.model.Category;
import com.deya.shopping_cart.model.Image;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int quantity;
    private String description;
    private Category category;
    private List<ImageDto> images;

    public ProductDto(Long id, String name, String brand, BigDecimal price, int quantity, String description, Category category, List<Image> images) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.category = category;
        this.images = images.stream()
                .map(image -> {
                    ImageDto imageDto = new ImageDto();
                    imageDto.setId(image.getId());
                    imageDto.setFileName(image.getFileName());
                    imageDto.setDownloadUrl(imageDto.getDownloadUrl());
                    return imageDto;
                }).toList();
    }
}
