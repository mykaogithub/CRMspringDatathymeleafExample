package com.kyanja.service;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kyanja.model.Customer;
import com.kyanja.repository.CustomerDatatableFilter;
import com.kyanja.repository.CustomerRepository;

@Service
public class CustomerService implements  ICustomerService {

    private final CustomerRepository customerRepository;
    


    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Page<Customer> getCustomersForDatatable(String queryString, Pageable pageable) {

        CustomerDatatableFilter customerDatatableFilter = new CustomerDatatableFilter(queryString);

     return customerRepository.findAll(customerDatatableFilter, pageable);
        
       
    }

	@Override
	public Customer findById(Long valueOf) {
		
		return customerRepository.findById(valueOf);
	}

	@Override
	public Customer save(@Valid Customer customerInstance) {
		
		return customerRepository.save(customerInstance);
	}

	@Override
	public void delete(Customer customerInstance) {
		
		customerRepository.delete(customerInstance);
		
	}
}