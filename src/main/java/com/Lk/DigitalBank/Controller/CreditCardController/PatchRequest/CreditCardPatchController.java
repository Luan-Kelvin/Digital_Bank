package com.Lk.DigitalBank.Controller.CreditCardController.PatchRequest;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.ChangeClosingInvoicePatchDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchBlockedDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.CreditCardPatchLimitDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPatch.UpdatePasswordDTO;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardPatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CreditCardPatchController {

    private final CreditCardPatchService creditCardPatchService;

    // BLOQUEAR CARTÃO
    @PatchMapping("/bloquear")
    public ResponseEntity<Void> blockCard(@RequestBody CreditCardPatchBlockedDTO dto){
        creditCardPatchService.blockCard(dto);
        return ResponseEntity.noContent().build();
    }

    // DESBLOQUEAR CARTÃO
    @PatchMapping("/desbloquear")
    public ResponseEntity<Void> cunlockCard(@RequestBody CreditCardPatchBlockedDTO dto){
        creditCardPatchService.unlockCard(dto);
        return ResponseEntity.noContent().build();
    }

    // ALTERAR SENHA DO CARTÃO
    @PatchMapping("/alterar/senha")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDTO dto){
        creditCardPatchService.updatePassword(dto);

        return ResponseEntity.ok().body("Senha alterada com sucesso!");
    }

    // AUMENTAR LIMITE
    @PatchMapping("/aumentar/limite")
    public ResponseEntity<CreditCardGetDTO> increaseLimit(@RequestBody CreditCardPatchLimitDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(creditCardPatchService.increaseLimit(dto));
    }

    // REDUZIR LIMITE
    @PatchMapping("diminuir/limite")
    public ResponseEntity<CreditCardGetDTO> reduceLimit(@RequestBody CreditCardPatchLimitDTO dto){
        return ResponseEntity.ok().body(creditCardPatchService.reduceLimit(dto));
    }

    // ALTERAR DATA DE FECHAMENTO DA FATURA
    @PatchMapping("alterar/fechamento")
    public ResponseEntity<String> changeInvoiceClosing(@Valid @RequestBody ChangeClosingInvoicePatchDTO dto){
        return ResponseEntity.ok().body("Data de fechamento alterado com sucesso!");
    }
}
