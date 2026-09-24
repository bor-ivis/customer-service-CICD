package com.avougjagi.customerservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(9L)
                .firstName("Alex")
                .lastName("Zeljic")
                .email("alex@test.se")
                .phone("0701234567")
                .build();

        customerDTO = CustomerDTO.builder()
                .firstName("Boris")
                .lastName("Ivis")
                .email("boris@test.se")
                .phone("+46123456789")
                .build();
    }

    @Test
    void findAll_shouldReturnCustomerDTOList() {

        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerDTO> result = customerService.findAll();

        assertEquals(1, result.size());
        assertEquals("Alex", result.get(0).getFirstName());
        assertEquals("Zeljic", result.get(0).getLastName());
    }

    @Test
    void findById_shouldReturnCustomerDTO_whenCustomerExists() {

        when(customerRepository.findById(9L)).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.findById(9L);

        assertNotNull(result);
        assertEquals(9L, result.getId());
        assertEquals("Alex", result.getFirstName());
    }

    @Test
    void findById_shouldReturnNull_whenCustomerDoesNotExist() {

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        CustomerDTO result = customerService.findById(99L);

        assertNull(result);
    }

    @Test
    void save_shouldSaveCustomer() {

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO result = customerService.save(customerDTO);

        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void delete_shouldReturnFalse_whenCustomerDoesNotExist() {

        when(customerRepository.existsById(1L)).thenReturn(false);

        boolean result = customerService.delete(1L);

        assertFalse(result);
        verify(customerRepository, never()).deleteById(anyLong());
    }
    @Mock
    private RestTemplate restTemplate;
    @Test
    void delete_shouldReturnTrue_whenCustomerExists() {

        when(customerRepository.existsById(9L)).thenReturn(true);
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(false);

        boolean result = customerService.delete(9L);

        assertTrue(result);
        verify(customerRepository, times(1)).deleteById(9L);
    }
}