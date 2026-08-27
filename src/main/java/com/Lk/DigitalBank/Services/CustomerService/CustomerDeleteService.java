package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDeleteService {
    private final Logger logger = LoggerFactory.getLogger(CustomerDeleteService.class);
    private final CustomerRepository customerRepository;

    public void deleteCustomner(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerDoesNotExistException(String.format("ERRO! Cliente com ID = %s não existe.", id)));

        customerRepository.delete(customer);
        logger.info(String.format("Cliente %s deletado com sucesso!", customer.getName()));
    }
}
