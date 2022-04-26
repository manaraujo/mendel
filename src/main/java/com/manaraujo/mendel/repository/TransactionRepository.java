package com.manaraujo.mendel.repository;

import com.manaraujo.mendel.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Long> getByTypes(String type);

    Transaction getById(Long transactionId);

    List<Transaction> getChildren(Long transactionId);
}
