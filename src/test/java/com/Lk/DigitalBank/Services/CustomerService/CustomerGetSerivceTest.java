package com.Lk.DigitalBank.Services.CustomerService;

import com.Lk.DigitalBank.Conversores.Conversor;
import com.Lk.DigitalBank.DTOs.Customer.CustomerGetDTO;
import com.Lk.DigitalBank.ENUM.CustomerStatus;
import com.Lk.DigitalBank.Entity.Customer;
import com.Lk.DigitalBank.Exception.CustomerDoesNotExistException;
import com.Lk.DigitalBank.Repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerGetSerivceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Conversor conversor;

    @InjectMocks
    private CustomerGetService customerGetService;

    @Test
    public void deveRetornarListaDeClientesAtivos(){
        Customer customer = new Customer();
        List<Customer> list = List.of(customer);
        CustomerGetDTO customerGetDTO = new CustomerGetDTO(1L, "Joaquim", LocalDate.of(2002, 2, 19), CustomerStatus.ACTIVE, List.of());

        when(customerRepository.searchActives()).thenReturn(list);
        when(conversor.converterCustomer(customer)).thenReturn(customerGetDTO);

        List<CustomerGetDTO> lista = customerGetService.listCustomerActives();

        assertEquals(1, lista.size());

        verify(customerRepository).searchActives();
        verify(conversor).converterCustomer(customer);
    }

    @Test
    public void deveRetornarListaDeClientesInativos(){
        Customer customer = new Customer();
        List<Customer> list = List.of(customer);
        CustomerGetDTO customerGetDTO = new CustomerGetDTO(1L, "Joaquim", LocalDate.of(2002, 2, 19), CustomerStatus.INACTIVE, List.of());

        when(customerRepository.searchInactives()).thenReturn(list);
        when(conversor.converterCustomer(customer)).thenReturn(customerGetDTO);

        List<CustomerGetDTO> lista = customerGetService.listCustomerInactives();

        assertEquals(1, lista.size());

        verify(customerRepository).searchInactives();
        verify(conversor).converterCustomer(customer);
    }

    @Test
    public void deveRetornarListaVaziaSeNaoTiverClientesAtivosCadastrados(){
        when(customerRepository.searchActives()).thenReturn(List.of());

        List<CustomerGetDTO> list = customerGetService.listCustomerActives();

        assertTrue(list.isEmpty());

        verify(customerRepository).searchActives();
        verify(conversor, never()).converterCustomer(any(Customer.class));
    }

    @Test
    public void deveRetornarListaVaziaSeNaoTiverClientesInativoCadastrados(){
        when(customerRepository.searchInactives()).thenReturn(List.of());

        List<CustomerGetDTO> list = customerGetService.listCustomerInactives();

        assertTrue(list.isEmpty());

        verify(customerRepository).searchInactives();
        verify(conversor, never()).converterCustomer(any(Customer.class));
    }

    @Test
    public void deveRetornarOClienteQueTenhaOIdSolicitado(){
        Customer customer = new Customer("Joaquim", "123.456.789-10", LocalDate.of(2004, 3, 12));
        CustomerGetDTO customerGetDTO = new CustomerGetDTO(1L, "Joaquim", LocalDate.of(2002, 2, 19), CustomerStatus.INACTIVE, List.of());

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(conversor.converterCustomer(customer)).thenReturn(customerGetDTO);

        CustomerGetDTO dto = customerGetService.findById(customer.getId());

        assertEquals(dto.name(), customer.getName());

        verify(customerRepository).findById(customer.getId());
        verify(conversor).converterCustomer(customer);
    }

    @Test
    public void develancarExecaoSeClienteNaoExistirNaBuscaPorId(){
        Long id = 1L;

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CustomerDoesNotExistException.class, () -> customerGetService.findById(id));

        verify(customerRepository).findById(id);
        verify(conversor, never()).converterCustomer(any(Customer.class));
    }


    @Test
    public void deveRetornarClienteComCpfIgualDaBusca(){
        String cpf = "123.456.789-10";
        Customer customer = new Customer("Joaquim", cpf,  LocalDate.of(2004, 3, 12));
        CustomerGetDTO customerGetDTO = new CustomerGetDTO(1L, "Joaquim", LocalDate.of(2002, 2, 19), CustomerStatus.INACTIVE, List.of());

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));
        when(conversor.converterCustomer(customer)).thenReturn(customerGetDTO);

        CustomerGetDTO dto = customerGetService.findByCPF(cpf);

        assertEquals("Joaquim", customer.getName());

        verify(customerRepository).findByCpf(cpf);
        verify(conversor).converterCustomer(customer);


    }

    @Test
    public void deveLancarExcecaoSeClienteNoaExistirNaBuscaPorCPF(){
        String cpf = "123.456.789-10";

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(CustomerDoesNotExistException.class, () -> customerGetService.findByCPF(cpf));

        verify(customerRepository).findByCpf(cpf);
        verify(conversor, never()).converterCustomer(any(Customer.class));
    }
}
