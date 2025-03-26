package com.productmanagement.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Use this POJO for offer service end point responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {

  private Long productId;

  private BigDecimal price;

  private String name;

  private String code;

  private String description;
   

}