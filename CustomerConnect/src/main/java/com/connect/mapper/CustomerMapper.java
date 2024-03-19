package com.connect.mapper;

import com.connect.data.dto.CustomerDto;
import com.connect.data.entity.Customer;
import org.mapstruct.Mapper;

import java.util.Set;


@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerDto dto);
    CustomerDto toDto(Customer entity);
    Set<Customer> toEntitys(Set<CustomerDto> dto);
    Set<CustomerDto> toDtos(Set<Customer> entity);
}
