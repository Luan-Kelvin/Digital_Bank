package com.Lk.DigitalBank.Controller.CustomerController.DeleteRequest;

import com.Lk.DigitalBank.Services.CustomerService.CustomerDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerDeleteController {
    private final CustomerDeleteService customerDeleteService;

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id){
        customerDeleteService.deleteCustomner(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
