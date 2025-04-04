package com.productmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.productmanagement.controller.dto.ProductDTO;
import com.productmanagement.model.SearchParameters;
import com.productmanagement.service.ProductService;
import com.productmanagement.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;


import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc()
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ProductControllerTest {

    @MockBean
    private ProductService productService;
    
    @Autowired
    private MockMvc mockMvc;
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    void createProduct() throws Exception {

        //GIVEN
        ProductDTO productDTO = TestUtils.buildProductDTO(10L);
        String expected = "{\"productId\":10,\"price\":10,\"name\":\"Product 1\",\"code\":\"PC123\",\"description\":\"Description for Product 1\"}";
        

        //WHEN
        when(productService.createProduct(productDTO)).thenReturn(
                productDTO);
        
        //THEN
        final MvcResult result = mockMvc.perform(post("/product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        assertEquals(expected, content);
    }



    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    void deleteOfferById_Success() throws Exception {

        //GIVEN
        Long id = 10L;

        //WHEN
        doNothing().when(productService).deleteProductById(id);

        //THEN
        mockMvc.perform(delete("/product/{id}",id.toString())
                                .with(csrf())
                        )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    void deleteOfferById_NotFound() throws Exception {

        //GIVEN
        Long id = 10L;

        //WHEN
        doThrow(new NoSuchElementException()).when(productService).deleteProductById(id);

        //THEN
        mockMvc.perform(delete("/product/{id}",id.toString())
                        .with(csrf())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user", roles={"USER"})
    void getProductById_Success() throws Exception {

        //GIVEN
        Long id = 10L;
        ProductDTO productDTO = TestUtils.buildProductDTO(1L);
        String expected = "{\"productId\":1,\"price\":10,\"name\":\"Product 1\",\"code\":\"PC123\",\"description\":\"Description for Product 1\"}";

        //WHEN
        when(productService.getProductById(id)).thenReturn(
                productDTO);

        //THEN
        MvcResult result = mockMvc.perform(get("/product/{id}",id.toString())
                        )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(expected, content);

    }

    @Test
    @WithMockUser(username = "user", roles={"USER"})
    void getProductById_NotFound() throws Exception {

        //GIVEN
        Long id = 10L;
        
        //WHEN
        when(productService.getProductById(id)).thenThrow(new NoSuchElementException());

        //THEN
        mockMvc.perform(get("/product/{id}",id.toString())
                )
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser("user")
    void getFilteredProducts() throws Exception {

        //GIVEN
        final ProductDTO dto = TestUtils.buildProductDTO(1L);
        final ProductDTO dto2 = TestUtils.buildProductDTO(2L);
        final SearchParameters searchParameters = TestUtils.buildSearchParameters();
        final String expected = "{\"content\":[{\"productId\":1,\"price\":10,\"name\":\"Product 1\",\"code\":\"PC123\",\"description\":\"Description for Product 1\"},{\"productId\":2,\"price\":10,\"name\":\"Product 1\",\"code\":\"PC123\",\"description\":\"Description for Product 1\"}],\"page\":{\"size\":10,\"number\":0,\"totalElements\":2,\"totalPages\":1}}";
        final Pageable pageRequest = PageRequest.of(searchParameters.getPageNumber(),
                searchParameters.getPageSize());
        final Page<ProductDTO> res = new PageImpl<>(List.of(dto,dto2), pageRequest, 2);
       
        //WHEN
        when(productService.findByCodeOrName(eq(searchParameters))).thenReturn(res);

        //THEN
        MvcResult result = mockMvc.perform(get("/product")
                        .queryParam("code",searchParameters.getCode())
                        .queryParam("name",searchParameters.getName())
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(expected, content);

    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    void updatePartialProduct_Success() throws Exception {

        //GIVEN
        Long id = 1L;
        ProductDTO productDTO = TestUtils.buildProductDTO(id);
        String expected = "{\"productId\":1,\"price\":10,\"name\":\"Product 1\",\"code\":\"PC123\",\"description\":\"Description for Product 1\"}";

        //WHEN
        when(productService.updatePartialProduct(id, productDTO)).thenReturn(
                productDTO);

        //THEN
        MvcResult result = mockMvc.perform(patch("/product/{id}",id.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDTO)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals(expected, content);

    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    void updatePartialProduct_NotFound() throws Exception {

        //GIVEN
        Long id = 1L;
        ProductDTO productDTO = TestUtils.buildProductDTO(id);

        //WHEN
        when(productService.updatePartialProduct(id, productDTO)).thenThrow(new NoSuchElementException());
               
        //THEN
        mockMvc.perform(patch("/product/{id}",id.toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productDTO)))
                .andExpect(status().isNotFound());
        
    }
    
}
