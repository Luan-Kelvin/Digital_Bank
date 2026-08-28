package com.Lk.DigitalBank.Controller.AccountController.PatchRequest;

import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPatchDTO;
import com.Lk.DigitalBank.Services.AccountService.AccounPatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountPatchController {

    private final AccounPatchService accounPatchService;


    // ALTERAR TYPE DA CONTA
    @PatchMapping("/type")
    public ResponseEntity<AccountGetDTO> alterType(@RequestBody AccountPatchDTO dto){

        return ResponseEntity.ok().body(accounPatchService.updateType(dto));
    }

    // BLOQUEAR CONTA
    @PatchMapping("/block/{id}")
    public ResponseEntity<Void> blockAccount(@PathVariable("id") Long id){
        accounPatchService.blockAccount(id);

        return ResponseEntity.noContent().build();
    }

}
