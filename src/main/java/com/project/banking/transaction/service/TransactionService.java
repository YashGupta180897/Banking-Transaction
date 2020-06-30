package com.project.banking.transaction.service;

import com.project.banking.transaction.model.Customer;
import com.project.banking.transaction.model.Transaction;
import com.project.banking.transaction.repository.CustomerRepository;
import com.project.banking.transaction.repository.TransactionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Set<String> flagSuspiciousTransactions(){

        List<Transaction> transactionList= (List<Transaction>) transactionRepository.findAll();
        List<Customer> customerList= (List<Customer>) customerRepository.findAll();

        Set<String> uniqueNumbers=new HashSet<>();

        Map<String, Customer> customerMap=new HashMap<>();
        for(Customer customer : customerList) {
            customerMap.put(customer.getAccountNumber(),customer);
        }

        transactionList.forEach( transaction -> {
            if( customerMap.get(transaction.getFromAccount()).getAddress().trim().equalsIgnoreCase(customerMap.get(transaction.getToAccount()).getAddress().trim())
                && customerMap.get(transaction.getFromAccount()).getPhoneNumber().trim().equalsIgnoreCase(customerMap.get(transaction.getToAccount()).getPhoneNumber().trim()) )
            {
                transaction.setSuspiciousTransaction(true);
                transactionRepository.save(transaction);

                customerMap.get(transaction.getFromAccount()).setSuspiciousAccount(true);
                customerRepository.save(customerMap.get(transaction.getFromAccount()));

                customerMap.get(transaction.getToAccount()).setSuspiciousAccount(true);
                customerRepository.save(customerMap.get(transaction.getToAccount()));

                uniqueNumbers.add(customerMap.get(transaction.getToAccount()).getPhoneNumber());
            }
        });
        return uniqueNumbers;
    }

    public JSONObject getSuspiciousTransactions() {

        Set<String> suspiciousPhoneNumbers=flagSuspiciousTransactions();

        List<Transaction> suspiciousTransactions= transactionRepository.findAllBySuspiciousTransactionTrue();
        Map<String, List<String>> suspicious= new HashMap<>();
        suspiciousTransactions.forEach( transaction ->{
            LocalDate localDate=transaction.getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            suspicious.put(new SimpleDateFormat("MMM").format(transaction.getTransactionDate()),getTransactionsForMonth(localDate,suspiciousTransactions));
        });

        //Suspicious Accounts
        List<List<String>> accountPairs=getSuspiciousAccount(suspiciousPhoneNumbers);

        JSONObject jsonObject=new JSONObject();
        suspicious.forEach((k, v) -> {
            try {
                jsonObject.put("Suspicious Transaction for the month of " + k, v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        int size=accountPairs.size();
        for(int i=0;i<size;i++){
            try {
                jsonObject.put("Suspicious Accounts Set "+(i+1), accountPairs.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObject;
    }

    private List<List<String>> getSuspiciousAccount(Set<String> uniqueNumbers){
        List<Customer> suspiciousCustomers= customerRepository.findAllBySuspiciousAccountTrue();
        List<List<String>> accountPairs=new ArrayList<>();
        AtomicInteger index= new AtomicInteger();
        uniqueNumbers.forEach( uniqueNumber ->{
            List<String> pairs=new ArrayList<>();
            suspiciousCustomers.forEach( customer -> {
                if(customer.getPhoneNumber().equalsIgnoreCase(uniqueNumber)){
                    pairs.add(customer.getAccountNumber());
                }
            });
            accountPairs.add(pairs);
            index.getAndIncrement();
        });
        return accountPairs;
    }

    private List<String> getTransactionsForMonth(LocalDate localDate, List<Transaction> suspiciousTransactions)
    {
        List<String> trans=new ArrayList<>();
        suspiciousTransactions.forEach( transaction -> {
            if(transaction.getTransactionDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue()==localDate.getMonthValue()){
                trans.add(transaction.getTransactionId());
            }
        });
        return trans;
    }

}
