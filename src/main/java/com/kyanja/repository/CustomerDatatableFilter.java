package com.kyanja.repository;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import com.kyanja.model.Customer;

import java.util.ArrayList;
import java.util.List;



public class CustomerDatatableFilter implements org.springframework.data.jpa.domain.Specification<Customer> {

	String userQuery;

	public CustomerDatatableFilter(String queryString) {
		this.userQuery = queryString;
	}

	public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> conditions = new ArrayList<Predicate>();
		CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
		Root<Customer> customerRoot = criteriaQuery.from(Customer.class);

		if (userQuery != null && userQuery != "") {
			conditions.add(criteriaBuilder.equal(customerRoot.get("firstName"), '%' + userQuery + '%'));
			conditions.add(criteriaBuilder.equal(customerRoot.get("lastName"), '%' + userQuery + '%'));
			conditions.add(criteriaBuilder.equal(customerRoot.get("city"), '%' + userQuery + '%'));
			conditions.add(criteriaBuilder.equal(customerRoot.get("emailAddress"), '%' + userQuery + '%'));
			conditions.add(criteriaBuilder.equal(customerRoot.get("phoneNumber"), '%' + userQuery + '%'));
			conditions.add(criteriaBuilder.equal(customerRoot.get("country"), '%' + userQuery + '%'));

		}

		return (!conditions.isEmpty() ? criteriaBuilder.or(conditions.toArray(new Predicate[conditions.size()]))
				: null);
	}

}