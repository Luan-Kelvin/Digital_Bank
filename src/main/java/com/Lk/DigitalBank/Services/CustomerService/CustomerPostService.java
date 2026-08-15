package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerPostDTO;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerAlreadyExistsException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerPostService {
    private final Logger logger = LoggerFactory.getLogger(CustomerPostService.class);
    private final Conversor conversor;
    private final CustomerRepository customerRepository;

    // CRIAR CUSTOMER
    public CustomerGetDTO createCustomer(CustomerPostDTO customerPostDTO){
        if (customerRepository.existsByCpf(customerPostDTO.cpf())){
            throw new CustomerAlreadyExistsException(String.format("ERRO! Cliente com CPF %s já existe.", customerPostDTO.cpf()));
        }

        Customer customer = new Customer(customerPostDTO.name(), customerPostDTO.cpf(), customerPostDTO.dateOfBirth());
        customerRepository.save(customer);

        logger.info(String.format("Cliente %s cadastrado com sucesso!", customer.getName()));

        return conversor.converterCustomer(customer);
    }
}
