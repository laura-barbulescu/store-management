package com.productmanagement.persistence.specification;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.model.SearchParameters;
import com.productmanagement.persistence.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
        
    private static Specification<ProductEntity> createSearchCriteria(String name, String code) {
        
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasLength(name)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%")));
            }
            if (StringUtils.hasLength(code)) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + code.toLowerCase() + "%")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
    
    public static Specification<ProductEntity> applyFilters(SearchParameters searchParameters) {

        return Specification.where(ProductSpecification.createSearchCriteria(searchParameters.getName(),
                searchParameters.getCode()));
    }
    
    
    
}
