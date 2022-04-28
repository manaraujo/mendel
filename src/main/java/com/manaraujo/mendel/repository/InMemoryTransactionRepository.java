package com.manaraujo.mendel.repository;

import com.manaraujo.mendel.exception.NotFoundException;
import com.manaraujo.mendel.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    Map<Long, Transaction> transactions = new HashMap<>();
    Map<Long, List<Transaction>> transactionWithChildren = new HashMap<>();
    Map<String, List<Long>> transactionIdsGroupedByType = new HashMap<>();

    @Override
    public void save(Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
        transactionWithChildren.put(transaction.getTransactionId(), new ArrayList<>());

        if (nonNull(transaction.getParentId())) {
            transactionWithChildren.get(transaction.getParentId()).add(transaction);
        }

        transactionIdsGroupedByType.computeIfAbsent(transaction.getType(), k -> new ArrayList<>()).add(transaction.getTransactionId());
    }

    @Override
    public List<Long> getIdsByType(String type) {
        return Optional.ofNullable(transactionIdsGroupedByType.get(type)).orElse(List.of());
    }

    @Override
    public Transaction getById(Long transactionId) {
        return Optional.ofNullable(transactions.get(transactionId))
                .orElseThrow(() -> new NotFoundException(String.format("Transaction with id %s not found", transactionId)));
    }

    @Override
    public List<Transaction> getChildren(Long transactionId) {
        return Optional.ofNullable(transactionWithChildren.get(transactionId)).orElse(List.of());
    }
}
