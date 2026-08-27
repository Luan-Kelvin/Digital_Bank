package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerDeleteServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerDeleteService customerDeleteService;

    @Test
    public void deveDeletarCustomer(){
        Long id = 1L;
        Customer customer = new Customer("Jonas", "123.456.789-10", LocalDate.of(2002, 10, 1));

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        customerDeleteService.deleteCustomner(id);

        verify(customerRepository).delete(customer);

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
