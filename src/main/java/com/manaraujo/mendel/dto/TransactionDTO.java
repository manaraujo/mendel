package com.manaraujo.mendel.dto;

import com.manaraujo.mendel.model.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.manaraujo.mendel.model.Transaction.buildTransaction;

@Getter
@Setter
@Data
@Builder
public class TransactionDTO {

    private Double amount;
    private String type;
    private Long parentId;

    public Transaction toTransaction(Long transactionId) {
        return buildTransaction(transactionId, amount, type, parentId);
    }

}