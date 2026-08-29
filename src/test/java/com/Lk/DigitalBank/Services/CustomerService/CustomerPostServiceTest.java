package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.DTOs.Customer.CustomerPostDTO;
import com.Lk.DigitalBank.ENUM.CustomerStatus;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerAlreadyExistsException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerPostServiceTest {

    @Mock
    private Conversor conversor;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerPostService customerPostService;

    @Test
    public void deveCriarNovoCustomerERetornarCustomerGetDTO(){
        Customer customer = new Customer("Joaquim", "555.424.825-22", LocalDate.of(2002, 2, 12));
        CustomerGetDTO customerGetDTO = new CustomerGetDTO(1L, "Joaquim", LocalDate.of(2002, 2, 12 ), CustomerStatus.INACTIVE, List.of());
        CustomerPostDTO postDto = new CustomerPostDTO("Joaquim", "555.424.825-22", LocalDate.of(2002, 2, 12));

        when(customerRepository.existsByCpf(customer.getCpf())).thenReturn(false);
        when(conversor.converterCustomer(customer)).thenReturn(customerGetDTO);

        CustomerGetDTO resultado = customerPostService.createCustomer(postDto);

        assertEquals(postDto.name(), resultado.name());
        assertEquals(postDto.dateOfBirth(), resultado.dateOfBirth());

        verify(customerRepository).existsByCpf(customer.getCpf());
        verify(customerRepository).save(any(Customer.class));
        verify(conversor).converterCustomer(customer);
    }

    @Test
    public void deveLancarExcecaoSeCustomerJaExistir(){
        String cpf = "123.456.789.10";
        CustomerPostDTO postDto = new CustomerPostDTO("Joaquim", cpf, LocalDate.of(2002, 3, 14));

        when(customerRepository.existsByCpf(cpf)).thenReturn(true);

        assertThrows(CustomerAlreadyExistsException.class, () -> customerPostService.createCustomer(postDto));

        verify(customerRepository).existsByCpf(cpf);
        verify(customerRepository, never()).save(any(Customer.class));
        verify(conversor, never()).converterCustomer(any(Customer.class));
    }
}
