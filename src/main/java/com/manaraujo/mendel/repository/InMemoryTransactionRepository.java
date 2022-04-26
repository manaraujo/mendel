package com.manaraujo.mendel.repository;

import com.manaraujo.mendel.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    @Override
    public void save(Transaction transaction) {}

    @Override
    public List<Long> getByTypes(String type) {
        return null;
    }

    @Override
    public Transaction getById(Long transactionId) {
        return null;
    }

    @Override
    public List<Transaction> getChildren(Long transactionId) {
        return null;
    }
}
