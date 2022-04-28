package com.manaraujo.mendel.service;

import com.manaraujo.mendel.model.Transaction;
import com.manaraujo.mendel.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository inMemoryTransactionRepository;

    public void saveTransaction(Transaction transaction) {
        inMemoryTransactionRepository.save(transaction);
    }

    public List<Long> getTransactionIdsByType(String type) {
        return inMemoryTransactionRepository.getIdsByType(type);
    }

    public Double sumTransactionAmountsTransitively(Long transactionId) {
        Transaction transaction = inMemoryTransactionRepository.getById(transactionId);
        List<Transaction> children = inMemoryTransactionRepository.getChildren(transactionId);

        Double childrenAmount = children.stream()
                .map(Transaction::getAmount)
                .mapToDouble(Double::doubleValue)
                .sum();

        return transaction.getAmount() + childrenAmount;
    }
}
