package com.productmanagement.service;

import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.mapper.ProductMapper;
import com.productmanagement.model.SearchParameters;
import com.productmanagement.persistence.entity.ProductEntity;
import com.productmanagement.persistence.repository.ProductRepository;
import com.productmanagement.persistence.specification.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.productmanagement.utils.TestUtils.buildProductDTO;
import static com.productmanagement.utils.TestUtils.buildProductEntity;
import static com.productmanagement.utils.TestUtils.buildSearchParameters;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper mapper;
    
    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void getProductById_Success(){

        //GIVEN
        final Long id = 10L;
        final ProductEntity entity = buildProductEntity(id);
        final ProductDTO dto = buildProductDTO(id);

        //WHEN
        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.map(entity)).thenReturn(dto);

        ProductDTO actualProduct = service.getProductById(id);

        //THEN
        assertEquals(dto, actualProduct);

    }

    @Test
    void getProductById_ThrowsException(){

        //GIVEN
        final Long id = 10L;

        //WHEN
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        //THEN
        assertThrows(NoSuchElementException.class, () -> service.getProductById(id));

    }


    @Test
    void deleteProductById_Success(){

        //GIVEN
        final Long id = 10L;

        //WHEN
        doNothing().when(productRepository).deleteById(id);

        service.deleteProductById(id);

        //THEN
        verify(productRepository,times(1)).deleteById(id);

    }

    @Test
    void createProduct_Success(){

        //GIVEN
        final Long id = 10L;
        final ProductEntity entity = buildProductEntity(id);
        final ProductDTO dto = buildProductDTO(id);
        
        //WHEN
        when(productRepository.save(entity)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(dto);
        when(mapper.map(dto)).thenReturn(entity);

        ProductDTO created = service.createProduct(dto);

        //THEN
        assertEquals(dto, created);

    }

    @Test
    void updatePartialProduct_Success(){

        //GIVEN
        final Long id = 10L;
        final ProductEntity entity = buildProductEntity(id);
        final ProductDTO dto = buildProductDTO(id);
        final BigDecimal newPrice = BigDecimal.valueOf(5);
        dto.setPrice(newPrice);
        
        //WHEN
        when(productRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productRepository.save(entity)).thenReturn(entity);
        when(mapper.map(entity)).thenReturn(dto);

        ProductDTO updated = service.updatePartialProduct(id, dto);

        //THEN
        assertEquals(newPrice, updated.getPrice());
        assertEquals(dto.getCode(), updated.getCode());
        assertEquals(dto.getName(), updated.getName());

    }

    @Test
    void updatePartialProduct_NotFound(){

        //GIVEN
        final Long id = 10L;
        final ProductDTO dto = buildProductDTO(id);
        
        //WHEN
        when(productRepository.findById(id)).thenThrow(new NoSuchElementException());


        Throwable exception = assertThrows(NoSuchElementException.class,
                ()->{service.updatePartialProduct(id, dto);} 
        );
        
        //THEN
        assertEquals(NoSuchElementException.class, exception.getClass());


    }

    @Test
    void findByCodeOrName_Success(){

        //GIVEN
        final Long id = 10L;
        final ProductEntity entity1 = buildProductEntity(id);
        final ProductEntity entity2 = buildProductEntity(5L);
        final ProductDTO dto1 = buildProductDTO(id);
        final ProductDTO dto2 = buildProductDTO(5L);
        final SearchParameters searchParameters = buildSearchParameters();

        final Pageable pageRequest = PageRequest.of(searchParameters.getPageNumber(),
                searchParameters.getPageSize());
        final List<ProductDTO> dtos = List.of(dto1, dto2);

        List<ProductEntity> entities = List.of(entity1, entity2);
        Page<ProductEntity> res = new PageImpl<>(entities, pageRequest,2 ); 
        ProductSpecification productSpecification = null;
        
        //WHEN
        MockedStatic<ProductSpecification> productSpecificationMockedStatic = Mockito.mockStatic(ProductSpecification.class);
            productSpecificationMockedStatic.when(() -> ProductSpecification.applyFilters(searchParameters))
                    .thenReturn(productSpecification);
           
                
        when(productRepository.findAll((Specification<ProductEntity>) eq(productSpecification), eq(pageRequest) )).thenReturn(res);
        when(mapper.map(entities)).thenReturn(dtos);

        Page<ProductDTO> results = service.findByCodeOrName(searchParameters);

        //THEN
        assertEquals(2, results.getTotalElements());
        assertEquals(dtos, results.getContent());


    }


    
    
}
