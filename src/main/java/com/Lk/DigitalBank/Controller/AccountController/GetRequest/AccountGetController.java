package com.Lk.DigitalBank.Controller.AccountController.GetRequest;

import com.Lk.DigitalBank.DTOs.Account.AccountGetDTO;
import com.Lk.DigitalBank.Services.AccountService.AccountGetService;
import lombok.RequiredArgsConstructor;
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

    // BUSCAR TODAS AS CONTAS CADASTRADAS
    @GetMapping
    public List<AccountGetDTO> listAccounts(){
        return accountGetService.listAccounts();
    }

    // BUSCR  POR ID
    @GetMapping("/id/{id}")
    public AccountGetDTO searchById(@PathVariable("id") Long id){
        return accountGetService.findById(id);
    }

    // BUSCAR POR NÚMERO CONTA
    @GetMapping("/number/{number}")
    public AccountGetDTO searchByAccountNumber(@PathVariable("number") String number){
        return accountGetService.findByAccountNumber(number);
    }

    // BUSCAR POR STATUS
    @GetMapping("/status/{status}")
    public List<AccountGetDTO> searchByStatus(@PathVariable("status") String status){
        return accountGetService.searchByStatus(status);
    }

    // BUSCAR POR TIPO DE CONTA
    @GetMapping("/type/{type}")
    public List<AccountGetDTO> searchByType(@PathVariable("type") String type){
        return accountGetService.searchByType(type);
    }

    // BUSCAR POR CPF DE CLIENTE
    @GetMapping("customer/cpf/{cpf}")
    public List<AccountGetDTO> searchByCpFCustomer(@PathVariable("cpf") String cpf){
        return accountGetService.searchByCustomer(cpf);
    }


}
