package com.Lk.DigitalBank.Controller.AccountController.PatchRequest;

import com.Lk.DigitalBank.Services.AccountService.AccounPatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountPatchController {

    private final AccounPatchService accounPatchService;

    @PatchMapping("/type/{accountNumber}/{type}")
    public ResponseEntity<Void> alterType(@PathVariable("accountNumber") String accountNumber, @PathVariable("type") String type){

        accounPatchService.updateType(type, accountNumber);

        return ResponseEntity.noContent().build();
    }

}
