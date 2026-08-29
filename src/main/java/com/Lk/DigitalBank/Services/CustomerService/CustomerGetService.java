package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerGetService {
    private final Logger logger = LoggerFactory.getLogger(CustomerGetService.class);
    private final CustomerRepository customerRepository;
    private final Conversor conversor;

    // LISTAR CLIENTES ATIVOS
    public List<CustomerGetDTO> listCustomerActives(){
        List<Customer> customers = customerRepository.searchActives();

        if (customers.isEmpty()){
            logger.info("Nenhum cliente cadastrado no banco.");
            return List.of();
        }

        List<CustomerGetDTO> dtos = customers.stream()
                .map(conversor::converterCustomer)
                .toList();

        return dtos;
    }

    // LISTAR CLIENTES ATIVOS
    public List<CustomerGetDTO> listCustomerInactives(){
        List<Customer> customers = customerRepository.searchInactives();

        if (customers.isEmpty()){
            logger.info("Nenhum cliente cadastrado no banco.");
            return List.of();
        }

        List<CustomerGetDTO> dtos = customers.stream()
                .map(conversor::converterCustomer)
                .toList();

        return dtos;
    }

    // BUSCAR POR ID
    public CustomerGetDTO findById(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerDoesNotExistException(String.format("ERRO! Cliente com ID = Nº %s não existe.", id)));

        return conversor.converterCustomer(customer);
    }

    // BUSCAR POR CPF
    public CustomerGetDTO findByCPF(String cpf){
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(() -> new CustomerDoesNotExistException(String.format("ERRO! Cliente com CPF: %s não existe", cpf)));

        return conversor.converterCustomer(customer);
    }


}
