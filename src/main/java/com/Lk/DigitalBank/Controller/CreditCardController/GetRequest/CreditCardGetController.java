package com.Lk.DigitalBank.Controller.CreditCardController.GetRequest;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CreditCardGetController {

    private final CreditCardGetService creditCardGetService;

    // LISTAR CARTÕES ATIVOS
    @GetMapping
    public ResponseEntity<List<CreditCardGetDTO>> listCards(){
        return ResponseEntity.ok().body(creditCardGetService.listCreditCards());
    }

    // LISTAR CARTÕES BLOQEUADOS
    @GetMapping("/bloqueados")
    public ResponseEntity<List<CreditCardGetDTO>> listCardsBlocked(){
        return ResponseEntity.ok().body(creditCardGetService.listCreditCardsBlockeds());
    }

    // LISTAR CARTÕES EXPIRADOS
    @GetMapping("/expirados")
    public ResponseEntity<List<CreditCardGetDTO>> listCardsExpired(){
        return ResponseEntity.ok().body(creditCardGetService.listCreditCardExpired());
    }

    // LISTAR CARTÕES CANCELADOS
    @GetMapping("/cancelados")
    public ResponseEntity<List<CreditCardGetDTO>> listCardsCanceled(){
        return ResponseEntity.ok().body(creditCardGetService.listCreditCardCanceled());
    }

    // BUSCAR CARTÃO POR NÚMERO
    @GetMapping("number/{number}")
    public ResponseEntity<CreditCardGetDTO> searchCreditCardByNumber(@PathVariable("number") String number){
        return ResponseEntity.ok().body(creditCardGetService.findByCardNumber(number));
    }


}
