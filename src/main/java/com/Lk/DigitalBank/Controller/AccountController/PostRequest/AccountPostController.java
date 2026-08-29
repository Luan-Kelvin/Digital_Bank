package com.Lk.DigitalBank.Controller.AccountController.PostRequest;

import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountPostDTO;
import com.Lk.DigitalBank.DTOs.Account.DepositAndWithDrawAccountDTO;
import com.Lk.DigitalBank.DTOs.Account.TransferPixDTO;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionGetDTO;
import com.Lk.DigitalBank.DTOs.Transaction.TransactionPixDTO;
import com.Lk.DigitalBank.Services.AccountService.AccountPostService;
import com.Lk.DigitalBank.Services.AccountService.AccountServiceGeneral;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountPostController {
    private final AccountPostService accountPostService;
    private final AccountServiceGeneral accountServiceGeneral;

    // CRIAR CONTA
    @PostMapping
    public ResponseEntity<AccountGetDTO> createAccount(@RequestBody @Valid AccountPostDTO dto){
        AccountGetDTO accountGetDTO = accountPostService.createAccount(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(accountGetDTO);
    }

    // DEPOSITAR DINHEIRO
    @PostMapping("/deposit")
    public ResponseEntity<TransactionGetDTO> deposit(@RequestBody @Valid DepositAndWithDrawAccountDTO dto){

        TransactionGetDTO transaction = accountServiceGeneral.deposit(dto.accountNumber(), dto.value());

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    // SACAR DINHEIRO
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionGetDTO> withdraw(@RequestBody @Valid DepositAndWithDrawAccountDTO dto){

        TransactionGetDTO transaction = accountServiceGeneral.withdraw(dto.accountNumber(), dto.value());

        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    // FAZER TRANSFERÊNCIA PIX
    @PostMapping("/pix")
    public ResponseEntity<TransactionPixDTO> transferPix(@RequestBody TransferPixDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(accountPostService.transferViaPix(dto));
    }

}
