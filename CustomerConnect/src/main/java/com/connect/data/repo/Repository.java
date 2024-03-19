package com.connect.data.repo;

import com.connect.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Customer, Long> {
    Customer findByUuid(UUID uuid);

}
