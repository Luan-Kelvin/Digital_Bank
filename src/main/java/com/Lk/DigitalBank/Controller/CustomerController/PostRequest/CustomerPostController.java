package com.Lk.DigitalBank.Controller.CustomerController.PostRequest;

import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerPostDTO;
import com.Lk.DigitalBank.Services.CustomerService.CustomerPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerPostController {

    private final CustomerPostService customerPostService;

    @PostMapping
    public ResponseEntity<CustomerGetDTO> createCustomer(@RequestBody CustomerPostDTO customerPostDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerPostService.createCustomer(customerPostDTO));
    }
}
