package com.productmanagement.service;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.model.SearchParameters;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductDTO product);

    Page<ProductDTO> findByCodeOrName(SearchParameters searchParameters);
    
    void deleteProductById(Long id);

    ProductDTO updatePartialProduct(Long id, ProductDTO product);
    
}
