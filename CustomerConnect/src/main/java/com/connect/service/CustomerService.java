package com.connect.service;

import com.connect.data.dto.CustomerDto;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CustomerService {
    CustomerDto CreateNewCustomer(String token, CustomerDto dto);
    CustomerDto GetDetails(String token);
    Set<CustomerDto> getAll();
    CustomerDto updateName(UUID customerUuid, String firstName, String lastName);
    void deleteCustomer(UUID uuid);
}
