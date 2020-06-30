package com.project.banking.transaction.controller;

import com.project.banking.transaction.service.TransactionService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TransactionAPI {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value ="${suspicious.transactions}")
    public ResponseEntity<String> getFaultyTransactions()
    {
        JSONObject suspiciousTransactions=transactionService.getSuspiciousTransactions();
        return new ResponseEntity<>(suspiciousTransactions.toString(), HttpStatus.OK);
    }

}
