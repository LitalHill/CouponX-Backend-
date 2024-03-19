package com.connect.service;

import com.connect.data.dto.UserDto;
import com.connect.mapper.CustomerMapper;
import com.connect.data.dto.CustomerDto;
import com.connect.data.entity.Customer;
import com.connect.data.repo.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private static final String AUTH_FORGE_TOKEN_URL = "http://auth-forge/parse-token";
    public final Repository customerRepository;
    public final RestTemplate restTemplate;
    private final CustomerMapper mapper;

    @Override
    public CustomerDto CreateNewCustomer(String token, CustomerDto dto) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(AUTH_FORGE_TOKEN_URL,
                HttpMethod.GET,
                httpEntity,
                UserDto.class);

        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {

            Customer newCustomer = Customer.builder()
                    .firstName(dto.firstName)
                    .lastName(dto.lastName)
                    .email(dto.email)
                    .uuid(responseEntity.getBody().getUuid())
                    .build();
            return mapper.toDto(customerRepository.save(newCustomer));
        }
        return null;
    }

    @Override
    public CustomerDto GetDetails(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        HttpEntity<String> response = new HttpEntity<>(httpHeaders);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(AUTH_FORGE_TOKEN_URL,
                HttpMethod.GET,
                response,
                UserDto.class);
        Customer customer = new Customer();

        customer.setUuid(Objects.requireNonNull(responseEntity.getBody()).getUuid());

        return mapper.toDto(customerRepository.findByUuid(customer.uuid));
    }

    @Override
    public Set<CustomerDto> getAll() {
        return mapper.toDtos(new HashSet<>(customerRepository.findAll()));
    }

    @Override
    public CustomerDto updateName(UUID customerUuid, String firstName, String lastName) {
        Customer customer = customerRepository.findByUuid(customerUuid);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        return mapper.toDto(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(UUID uuid) {
        Customer customer = customerRepository.findByUuid(uuid);
        customerRepository.deleteById(customer.id);
    }
}
