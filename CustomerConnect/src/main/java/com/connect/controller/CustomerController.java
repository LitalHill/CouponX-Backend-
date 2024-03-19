package com.connect.controller;

import com.connect.data.dto.CustomerDto;
import com.connect.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    public final CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createCustomer(
            @RequestHeader("Authorization") String token,@RequestBody CustomerDto customerDto){
        CustomerDto newCustomer = customerService.CreateNewCustomer(token, customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCustomer);
    }

    @GetMapping("/details")
    public ResponseEntity<CustomerDto> getDetails(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(customerService.GetDetails(token));
    }

    @GetMapping("/all")
    public ResponseEntity<Set<CustomerDto>> getAll(){
        return ResponseEntity.ok(customerService.getAll());
    }

    @PutMapping("/change/{uuid}")
    public ResponseEntity<CustomerDto> changeName(@PathVariable UUID uuid, String firstName, String lastNAme){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerService.updateName(uuid,firstName, lastNAme));
    }

    @DeleteMapping("/delete/{uuid}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID uuid){
        customerService.deleteCustomer(uuid);
        return ResponseEntity.noContent().build();
    }
}
