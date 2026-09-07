package com.Lk.DigitalBank.Controller.AccountController.GetRequest;

import com.Lk.DigitalBank.DTOs.Account.AccountBalanceDTO;
import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.Services.AccountService.AccountGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("accounts")
@RequiredArgsConstructor
public class AccountGetController {
    private final AccountGetService accountGetService;

    // BUSCAR TODAS AS CONTAS ATIVAS
    @GetMapping
    public ResponseEntity<List<AccountGetDTO>> listAccountsActives(){
        return ResponseEntity.ok().body(accountGetService.listAccountsAcitives());


    }

    // BUSCAR TODAS AS CONTAS INATIVAS
    @GetMapping("/inativas")
    public ResponseEntity<List<AccountGetDTO>> listAccountsInactives(){
        return ResponseEntity.ok().body(accountGetService.listAccountsInactive());
    }

    // BUSCR  POR ID
    @GetMapping("/id/{id}")
    public ResponseEntity<AccountGetDTO> searchById(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(accountGetService.findById(id));
    }

    // CONSULTAR SALDO
    @GetMapping("/saldo/{accountNumber}")
    public ResponseEntity<AccountBalanceDTO> checkBalance(@PathVariable("accountNumber") String accountNumber){
        return ResponseEntity.status(HttpStatus.OK).body(accountGetService.checkBalance(accountNumber));
    }

    // BUSCAR POR NÚMERO CONTA
    @GetMapping("/number/{number}")
    public ResponseEntity<AccountGetDTO> searchByAccountNumber(@PathVariable("number") String number){
        return ResponseEntity.ok().body(accountGetService.findByAccountNumber(number));
    }

    // BUSCAR POR STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AccountGetDTO>> searchByStatus(@PathVariable("status") String status){
        return ResponseEntity.ok().body(accountGetService.searchByStatus(status));
    }

    // BUSCAR POR TIPO DE CONTA
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AccountGetDTO>> searchByType(@PathVariable("type") String type){
        return ResponseEntity.ok().body(accountGetService.searchByType(type));
    }

    // BUSCAR POR CPF DE CLIENTE
    @GetMapping("customer/cpf/{cpf}")
    public ResponseEntity<List<AccountGetDTO>> searchByCpFCustomer(@PathVariable("cpf") String cpf){
        return ResponseEntity.ok().body(accountGetService.searchByCustomer(cpf));
    }


}
