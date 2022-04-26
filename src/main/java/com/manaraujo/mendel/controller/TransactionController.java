package com.manaraujo.mendel.controller;

import com.manaraujo.mendel.dto.TransactionDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/transactions")
@RestController
public class TransactionController {

    @PutMapping("/{transaction_id}")
    public @ResponseBody TransactionDTO saveTransaction(
            @RequestBody TransactionDTO transactionDTO,
            @PathVariable(value = "transaction_id") Long transactionId) {
        return null;
    }

    @GetMapping("/types/{type}")
    public @ResponseBody List<Long> getTransactionsByType(@PathVariable String type) {
        return null;
    }

    @GetMapping("/sum/{transaction_id}")
    public @ResponseBody List<Long> sumTransactionAmountsTransitively(@PathVariable( value = "transaction_id") Long transactionId) {
        return null;
    }

}
