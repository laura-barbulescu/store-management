package com.productmanagement.model.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Error POJO
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

    private String reasonCode;

    private String description;


}

