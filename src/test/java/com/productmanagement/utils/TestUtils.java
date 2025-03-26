package com.productmanagement.utils;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.model.SearchParameters;
import com.productmanagement.persistence.entity.ProductEntity;

import java.math.BigDecimal;

public class TestUtils {

    public static ProductEntity buildProductEntity(Long id) {
        final ProductEntity entity = new ProductEntity();
        entity.setProductId(id);
        entity.setCode("PC123");
        entity.setName("Product 1");
        entity.setDescription("Description for Product 1");
        entity.setPrice(BigDecimal.valueOf(10L));

        return entity;
    }

    public static ProductDTO buildProductDTO(Long id) {
        final ProductDTO entity = new ProductDTO();
        entity.setProductId(id);
        entity.setCode("PC123");
        entity.setName("Product 1");
        entity.setDescription("Description for Product 1");
        entity.setPrice(BigDecimal.valueOf(10L));

        return entity;
    }

    public static SearchParameters buildSearchParameters(){
        return SearchParameters.builder()
                .code("code")
                .name("name")
                .pageNumber(0)
                .pageSize(10)
                .build();
    }
}
