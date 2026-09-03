package com.Lk.DigitalBank.Controller.CreditCardController.PostRequest;

import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardGetDTO;
import com.Lk.DigitalBank.DTOs.CreditCard.CreditCardPostDTO;
import com.Lk.DigitalBank.Services.CreditCardService.CreditCardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("card")
@RequiredArgsConstructor
public class CreditCardPostController {

    private final CreditCardPostService creditCardPostService;

    @PostMapping("/create")
    public ResponseEntity<CreditCardGetDTO> createCard(@RequestBody CreditCardPostDTO dto){
        return ResponseEntity.ok().body(creditCardPostService.createCreditCard(dto));
    }
}
