package com.project.banking.transaction.repository;

import com.project.banking.transaction.model.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    List<Transaction> findAllBySuspiciousTransactionTrue();
}
