package com.manaraujo.mendel.service;

import com.manaraujo.mendel.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    public void saveTransaction(Transaction transaction) {}

    public List<Long> getByType(String type) {
        return null;
    }

    public Double sumTransactionAmountsTransitively(Long transactionId) {
        return null;
    }
}
