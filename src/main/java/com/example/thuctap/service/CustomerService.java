package com.example.thuctap.service;

import com.example.thuctap.models.CustomerRegisterRequest;
import com.example.thuctap.models.CustomerLoginRequest;
import com.example.thuctap.models.Customer;
import com.example.thuctap.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer register(CustomerRegisterRequest request) {
        Optional<Customer> existing = customerRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPassword(request.getPassword()); // TODO: mã hoá mật khẩu

        return customerRepository.save(customer);
    }

    public Customer login(CustomerLoginRequest request) {
        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!customer.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        return customer;
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
    }

    public Customer findById(int id) {
        return customerRepository.findById(id).orElse(null);
    }

}