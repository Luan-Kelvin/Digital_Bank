package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerPatchDTO;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Exception.NameSameThePreviousOneException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerPatchService {
    private final Logger logger = LoggerFactory.getLogger(CustomerPatchService.class);
    private final CustomerRepository customerRepository;
    private final Conversor conversor;

    // ATUALIZAR NOME
    public CustomerGetDTO updateName(CustomerPatchDTO dto){
        Customer customer = customerRepository.findById(dto.id())
                .orElseThrow(() -> new CustomerDoesNotExistException(String.format("ERRO! Cliente com ID = %s não existe.", dto.id())));

        if (customer.getName().equalsIgnoreCase(dto.nome())){
            throw new NameSameThePreviousOneException(String.format("ERRO! Nomes Iguais %s = %s ", customer.getName(), dto.nome()));
        }

        customer.setName(dto.nome());
        logger.info(String.format("Nome alterado com sucesso! Antigo: %s | Novo: %s", customer.getName(), dto.nome()));
        customerRepository.save(customer);

        return conversor.converterCustomer(customer);
    }
}
