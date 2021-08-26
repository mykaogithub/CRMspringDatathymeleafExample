package com.kyanja.repository;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.kyanja.model.Customer;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {



	Customer findById(Long valueOf);

	@SuppressWarnings("unchecked")
	Customer save(@Valid Customer customerInstance);

	void delete(Customer customerInstance);

}
