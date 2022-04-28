package com.manaraujo.mendel.controller;

import com.manaraujo.mendel.dto.TransactionDTO;
import com.manaraujo.mendel.model.Transaction;
import com.manaraujo.mendel.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PutMapping("/{transaction_id}")
    public @ResponseBody ResponseEntity<Map<String, String>> saveTransaction(
            @RequestBody TransactionDTO transactionDTO,
            @PathVariable(value = "transaction_id") Long transactionId) {
        Transaction transaction = transactionDTO.toTransaction(transactionId);
        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @GetMapping("/types/{type}")
    public @ResponseBody List<Long> getTransactionIdsByType(@PathVariable String type) {
        return transactionService.getTransactionIdsByType(type);
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> sumTransactionAmountsTransitively(
            @PathVariable( value = "transaction_id") Long transactionId) {
        Double totalAmount = transactionService.sumTransactionAmountsTransitively(transactionId);
        return ResponseEntity.ok(Map.of("sum", totalAmount));
    }

}
