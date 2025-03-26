package com.productmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SearchParameters {
    private String code;
    
    private String name;
    
    private Integer pageNumber;
    
    private Integer pageSize;
    
}
