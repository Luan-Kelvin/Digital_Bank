package com.Lk.DigitalBank.Controller.CustomerController.PatchRequest;

import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerPatchDTO;
import com.Lk.DigitalBank.Services.CustomerService.CustomerPatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerPatchController {

    private final CustomerPatchService customerPatchService;

    @PatchMapping("/name")
    public ResponseEntity<CustomerGetDTO> updateName(@RequestBody CustomerPatchDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(customerPatchService.updateName(dto));
    }
}
