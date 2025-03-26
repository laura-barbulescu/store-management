package com.productmanagement.mapper;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper between ProductEntity and ProductDTO
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

 
    ProductDTO map(ProductEntity entity);

    ProductEntity map(ProductDTO productDTO);

    List<ProductDTO> map(List<ProductEntity> entities);
    
}
