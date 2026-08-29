package com.Lk.DigitalBank.Controller.CustomerController.GetRequest;

import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.Services.CustomerService.CustomerGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerGetController {
    private final CustomerGetService customerGetService;

    @GetMapping
    public ResponseEntity<List<CustomerGetDTO>> listCustomersActives(){
        return ResponseEntity.status(HttpStatus.OK).body(customerGetService.listCustomerActives());
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<CustomerGetDTO>> listCustomersInactives(){
        return ResponseEntity.status(HttpStatus.OK).body(customerGetService.listCustomerInactives());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<CustomerGetDTO> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(customerGetService.findById(id));
    }
}
