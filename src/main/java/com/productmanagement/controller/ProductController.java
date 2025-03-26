package com.productmanagement.controller;

import com.productmanagement.controller.dto.ProductDTO;

import com.productmanagement.model.SearchParameters;
import com.productmanagement.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for REST operations on Products
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {

  private final ProductService productService;

  /**
   * Creates a new product based on the request data provided
   * @param product   
   * @return ProductDTO - the newly created resource 
   */
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @RequestMapping(value = "/product", method = RequestMethod.POST, consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public ProductDTO createNewProduct(@RequestBody @Valid ProductDTO product) {

    log.info("Creating new product {}", product);
    return productService.createProduct(product);

  }

  /**
   * Deletes the product based on the ID provided as a path parameter
   * @param id - the unique identifier of the product that we want to delete
   */
  @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteProductById(@PathVariable Long id) {

    log.info("Deleting product with ID {}", id);
    
    productService.deleteProductById(id);

  }

  /**
   * Returns a list of products filtered by name or code. In case no filters are passed then it will return the entire list of products
   * @param name - the name of a product that we want to filter by
   * @param code - the code of a product that we want to filter by
   * @return List<ProductDTO> - the list of products matching the filter criteria
   */
  @RequestMapping(value = "/product", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
  public Page<ProductDTO> getAllProductsByCodeOrName(@RequestParam(required = false, name = "name") String name,
                                                     @RequestParam(required = false, name = "code") String code,
                                                     @RequestParam(required = false, name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                     @RequestParam(required = false, name = "pageSize", defaultValue = "10") Integer pageSize) {


    SearchParameters searchParameters = SearchParameters.builder()
                    .code(code)
            .name(name)
            .pageNumber(pageNumber)
            .pageSize(pageSize)
            .build();
    
    return productService.findByCodeOrName(searchParameters);

  }

  /**
   * Returns a product based on a unique identifier
   * @param id - the unique identifier of the product we are searching for
   * @return ProductDTO - the product requested
   */
  @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
  public ProductDTO getProductById(@PathVariable Long id) {

    return productService.getProductById(id);
  }

  /**
   * Partially updates a product with the specified new values in the request 
    * @param id - The unique identifier of the product we want to update
   * @param product - The new values we want to set for the product
   * @return ProductDTO - The product with its updated values
   */  
  @RequestMapping(value = "/product/{id}", method = RequestMethod.PATCH, consumes = "application/json")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ProductDTO partialUpdateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO product) {

    log.info("Partially updating product with ID {} and delta {}", id, product);
    
    return productService.updateProduct(id, product);
  }
}
