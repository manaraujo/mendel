package com.manaraujo.mendel.repository;

import com.manaraujo.mendel.exception.NotFoundException;
import com.manaraujo.mendel.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {

    Map<Long, Transaction> transactions = new HashMap<>();
    Map<Long, Set<Transaction>> transactionWithChildren = new HashMap<>();
    Map<String, Set<Long>> transactionIdsGroupedByType = new HashMap<>();

    @Override
    public void save(Transaction transaction) {
        validateIfParentExists(transaction.getParentId());

        transactions.put(transaction.getTransactionId(), transaction);
        transactionWithChildren.put(transaction.getTransactionId(), new HashSet<>());

        if (nonNull(transaction.getParentId())) {
            transactionWithChildren.get(transaction.getParentId()).add(transaction);
        }

        transactionIdsGroupedByType.computeIfAbsent(transaction.getType(), k -> new HashSet<>()).add(transaction.getTransactionId());
    }

    @Override
    public Set<Long> getIdsByType(String type) {
        return Optional.ofNullable(transactionIdsGroupedByType.get(type)).orElse(Set.of());
    }

    @Override
    public Transaction getById(Long transactionId) {
        return Optional.ofNullable(transactions.get(transactionId))
                .orElseThrow(() -> new NotFoundException(String.format("Transaction with id %s not found", transactionId)));
    }

    @Override
    public Set<Transaction> getChildren(Long transactionId) {
        return Optional.ofNullable(transactionWithChildren.get(transactionId)).orElse(Set.of());
    }

    private void validateIfParentExists(Long parentId) {
        if (nonNull(parentId) && isNull(transactions.get(parentId))) {
            throw new NotFoundException(String.format("Parent_id %s not found", parentId));
        }
    }
}
