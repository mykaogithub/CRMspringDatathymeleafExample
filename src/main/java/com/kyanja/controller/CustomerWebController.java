package com.kyanja.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyanja.model.Customer;
import com.kyanja.service.ICustomerService;

@Controller
public class CustomerWebController {
	
	@Autowired
	ICustomerService customerService;



	@RequestMapping("/")
	public String welcomePage() {
		return "index.html";
	}
	
	@RequestMapping("/customer")
	public String index() {
		return "/customer/index.html";
	}

	@RequestMapping(value = "/customer/data_for_datatable", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getDataForDatatable(@RequestParam Map<String, Object> params) {
		int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
		int length = params.containsKey("length") ? Integer.parseInt(params.get("length").toString()) : 30;
		int start = params.containsKey("start") ? Integer.parseInt(params.get("start").toString()) : 30;
		int currentPage = start / length;

		String sortName = "id";
		String dataTableOrderColumnIdx = params.get("order[0][column]").toString();
		String dataTableOrderColumnName = "columns[" + dataTableOrderColumnIdx + "][data]";
		if (params.containsKey(dataTableOrderColumnName))
			sortName = params.get(dataTableOrderColumnName).toString();
		String sortDir = params.containsKey("order[0][dir]") ? params.get("order[0][dir]").toString() : "asc";

		Sort.Order sortOrder = new Sort.Order((sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC),
				sortName);

		List<Order> orders = new ArrayList<Order>();
		orders.add(sortOrder);

		Pageable pageable = new PageRequest(currentPage, length, new Sort(orders));

		String queryString = (String) (params.get("search[value]"));

		Page<Customer> customers = customerService.getCustomersForDatatable(queryString, pageable);

		long totalRecords = customers.getTotalElements();

		List<Map<String, Object>> cells = new ArrayList<>();
		customers.forEach(customer -> {
			Map<String, Object> cellData = new HashMap<>();
			cellData.put("id", customer.getId());
			cellData.put("firstName", customer.getFirstName());
			cellData.put("lastName", customer.getLastName());
			cellData.put("emailAddress", customer.getEmailAddress());
			cellData.put("city", customer.getCity());
			cellData.put("country", customer.getCountry());
			cellData.put("phoneNumber", customer.getPhoneNumber());
			cells.add(cellData);
		});

		Map<String, Object> jsonMap = new HashMap<>();

		jsonMap.put("draw", draw);
		jsonMap.put("recordsTotal", totalRecords);
		jsonMap.put("recordsFiltered", totalRecords);
		jsonMap.put("data", cells);

		String json = null;
		try {
			json = new ObjectMapper().writeValueAsString(jsonMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;
	}

	@RequestMapping("/customer/edit/{id}")
	public String edit(@PathVariable String id, Model model) {
		Customer customerInstance = customerService.findById(Long.valueOf(id));

		model.addAttribute("customerInstance", customerInstance);

		return "/customer/edit.html";
	}

	@RequestMapping(value = "/customer/update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("customerInstance") Customer customerInstance,
			BindingResult bindingResult, Model model, RedirectAttributes atts) {
		if (bindingResult.hasErrors()) {
			return "/customer/edit.html";
		} else {
			if (customerService.save(customerInstance) != null)
				atts.addFlashAttribute("message", "Customer updated successfully");
			else
				atts.addFlashAttribute("message", "Customer update failed.");

			return "redirect:/customer/";
		}
	}

	@RequestMapping("/create")
	public String create(Model model) {
		model.addAttribute("customerInstance", new Customer());
		return "/customer/create.html";
	}
	
	

	@RequestMapping(value = "/customer/save", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("customerInstance") Customer customerInstance,
			BindingResult bindingResult, Model model, RedirectAttributes atts) {
		if (bindingResult.hasErrors()) {
			return "/customer/create.html";
		} else {
			if (customerService.save(customerInstance) != null)
				atts.addFlashAttribute("message", "Customer created successfully");
			else
				atts.addFlashAttribute("message", "Customer creation failed.");

			return "redirect:/customer/";
		}
	}

	@RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
	public String delete(@RequestParam Long id, RedirectAttributes atts) {

		try {

			Customer customerInstance = customerService.findById(id);
			customerService.delete(customerInstance);

		} catch (Exception e) {
			new IllegalArgumentException("Customer Not Found:" + id);

		}

		atts.addFlashAttribute("message", "Customer deleted.");

		return "redirect:/customer/";
	}

}