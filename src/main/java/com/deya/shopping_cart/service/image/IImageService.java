package com.deya.shopping_cart.service.image;

import com.deya.shopping_cart.DTO.ImageDto;
import com.deya.shopping_cart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);
    void updateImage(MultipartFile file, Long id);

    ImageDto convertImageToDto(Image images);
}
