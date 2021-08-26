package com.kyanja.service;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kyanja.model.Customer;

public interface ICustomerService {

	public Page<Customer> getCustomersForDatatable(String queryString, Pageable pageable);

	Customer findById(Long valueOf);

	Customer save(@Valid Customer customerInstance);

	void delete(Customer customerInstance);

}
