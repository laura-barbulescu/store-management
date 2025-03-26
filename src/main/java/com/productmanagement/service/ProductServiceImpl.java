package com.productmanagement.service;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.mapper.ProductMapper;
import com.productmanagement.model.SearchParameters;
import com.productmanagement.persistence.entity.ProductEntity;
import com.productmanagement.persistence.repository.ProductRepository;
import com.productmanagement.persistence.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    
    private final ProductRepository productRepository;
    
    
    @Override
    public Page<ProductDTO> findByCodeOrName(SearchParameters searchParameters) {

        Pageable pageRequest = PageRequest.of(searchParameters.getPageNumber(), 
                searchParameters.getPageSize());

        
        Page<ProductEntity> results = productRepository.findAll(Specification.where(ProductSpecification.filterProducts(searchParameters.getName(), 
                                        searchParameters.getCode())
                        ), pageRequest
                );
       
        List<ProductDTO> dtos = productMapper.map(results.get().toList());
        return new PageImpl<>(dtos, pageRequest, results.getTotalElements());
    }
    
    @Override
    public ProductDTO getProductById(final Long id) {

        return productMapper.map(
                productRepository.findById(id).orElseThrow()
        );
    }

    @Override
    public ProductDTO createProduct(final ProductDTO product) {

        return productMapper.map(
                productRepository.save(productMapper.map(product))
        );
    }

    @Override
    public void deleteProductById(final Long id) {

        productRepository.deleteById(id);
    }

    @Override
    public ProductDTO updateProduct(final Long id, final ProductDTO updatedProduct) {

        return productMapper.map(productRepository.findById(id)
                .map(prod -> {
                    if (StringUtils.hasLength(updatedProduct.getCode())) 
                        prod.setCode(updatedProduct.getCode());
                    if (updatedProduct.getPrice() != null)
                        prod.setPrice(updatedProduct.getPrice());
                    if (StringUtils.hasLength(updatedProduct.getName()))
                        prod.setName(updatedProduct.getName());
                    if (StringUtils.hasLength(updatedProduct.getDescription()))
                        prod.setDescription(updatedProduct.getDescription());
                    return productRepository.save(prod);
                })
                .orElseThrow());
       
    }


}
