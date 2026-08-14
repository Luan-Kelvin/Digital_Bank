package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerGetService {
    private final Logger logger = LoggerFactory.getLogger(CustomerGetService.class);
    private CustomerRepository customerRepository;
    private final Conversor conversor;

    // LISTAR CLIENTES EXISTENTES
    public List<CustomerGetDTO> listCustomer(){
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()){
            logger.info("Nenhum cliente cadastrado no banco.");
        }

        List<CustomerGetDTO> dtos = new ArrayList<>();

        customers.forEach(c -> {
            dtos.add(conversor.converterCustomer(c));
        });

        return dtos;
    }
}
