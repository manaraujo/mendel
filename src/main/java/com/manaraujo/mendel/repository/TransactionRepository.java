package com.manaraujo.mendel.repository;

import com.manaraujo.mendel.model.Transaction;

import java.util.Set;

public interface TransactionRepository {

    void save(Transaction transaction);

    Set<Long> getIdsByType(String type);

    Transaction getById(Long transactionId);

    Set<Transaction> getChildren(Long transactionId);
}
