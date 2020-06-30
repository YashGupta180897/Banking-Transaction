package com.project.banking.transaction.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "from_account")
    private String fromAccount;

    @Column(name = "to_account")
    private String toAccount;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "suspicious_transaction")
    private Boolean suspiciousTransaction;

    public Boolean getSuspiciousTransaction() {
        return suspiciousTransaction;
    }

    public void setSuspiciousTransaction(Boolean suspiciousTransaction) {
        this.suspiciousTransaction = suspiciousTransaction;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
