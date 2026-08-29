package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Services.AccountService.AccountPatchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDeleteService {
    private final Logger logger = LoggerFactory.getLogger(CustomerDeleteService.class);
    private final AccountPatchService accountPatchService;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public void deleteCustomner(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerDoesNotExistException(String.format("ERRO! Cliente com ID = %s não existe.", id)));

        customer.getAccounts().forEach(ac -> {
            accountPatchService.blockAccount(ac.getId());
            accountRepository.save(ac);
        });

        customer.inactive();
        customerRepository.save(customer);
        logger.info(String.format("Cliente %s deletado com sucesso!", customer.getName()));
    }
}
