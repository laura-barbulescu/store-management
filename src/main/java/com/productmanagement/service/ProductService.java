package com.productmanagement.service;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.model.SearchParameters;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO product);

    Page<ProductDTO> findByCodeOrName(SearchParameters searchParameters);
    
    void deleteProductById(Long id);

    ProductDTO updateProduct(Long id, ProductDTO product);
    
}
