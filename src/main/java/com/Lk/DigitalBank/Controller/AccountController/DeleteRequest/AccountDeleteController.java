package com.Lk.DigitalBank.Controller.AccountController.DeleteRequest;

import com.Lk.DigitalBank.Services.AccountService.AccountDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts/")
@RequiredArgsConstructor
public class AccountDeleteController {

    private final AccountDeleteService accountDeleteService;

    @DeleteMapping("/delete/{accountNumber}")
    public ResponseEntity<Void> deleetAccount(@PathVariable("accountNumber") String accountNumber){
        accountDeleteService.deleteAccount(accountNumber);

        return ResponseEntity.noContent().build();
    }
}
