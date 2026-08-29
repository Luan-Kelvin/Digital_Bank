package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.ENUM.AccountStatus;
import com.Lk.DigitalBank.ENUM.AccountType;
import com.Lk.DigitalBank.ENUM.CustomerStatus;
import com.Lk.DigitalBank.Entity.Account;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.AccountRepository;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import com.Lk.DigitalBank.Services.AccountService.AccountPatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerDeleteServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountPatchService accountPatchService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CustomerDeleteService customerDeleteService;

    @Test
    public void deveDeixarCustomerComStatusInativo(){
        Long id = 1L;
        Customer customer = new Customer("Jonas", "123.456.789-10", LocalDate.of(2002, 10, 1));
        Account account = new Account(customer, AccountType.CURRENT);
        customer.addAccount(account);
        account.addCustomer(customer);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerDeleteService.deleteCustomner(id);

        assertEquals(CustomerStatus.INACTIVE, customer.getCustomerStatus());

        verify(accountPatchService).blockAccount(account.getId());
        verify(accountRepository).save(any(Account.class));
        verify(customerRepository).save(any(Customer.class));

    }

    @Test
    public void develancarExcecaoSeClienteNaoExistir(){
        Long id = 1L;
        Customer customer = new Customer();

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerDoesNotExistException.class, () -> customerDeleteService.deleteCustomner(id));

        verify(customerRepository, never()).delete(any(Customer.class));
    }
}
