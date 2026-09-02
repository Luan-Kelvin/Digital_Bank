package com.Lk.DigitalBank.Controller.CreditCardController.PatchRequest;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("card/")
@RequiredArgsConstructor
public class CreditCardPatchController {

    private final CreditCardPatchService creditCardPatchService;

    // BLOQUEAR CARTÃO
    @PatchMapping("bloquear/")
    public ResponseEntity<Void> blockCard(@RequestBody CreditCardPatchBlockedDTO dto){
        creditCardPatchService.blockCard(dto);
        return ResponseEntity.noContent().build();
    }
}
