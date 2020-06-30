package com.project.banking.transaction.repository;

import com.project.banking.transaction.model.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, String> {

    List<Customer> findAllBySuspiciousAccountTrue();

}
