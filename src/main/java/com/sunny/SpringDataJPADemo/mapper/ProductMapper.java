package com.sunny.SpringDataJPADemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.sunny.SpringDataJPADemo.dto.ProductDTO;
import com.sunny.SpringDataJPADemo.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Spring will automatically inject the generated version of this interface!
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Convert Entity to DTO
    ProductDTO toDto(Product product);

    // Convert incoming DTO payload to a Database Entity
    Product toEntity(ProductDTO productDTO);
}
