package com.manaraujo.mendel.dto;

import com.manaraujo.mendel.model.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class TransactionDTO {

    private Long transactionId;
    private Double amount;
    private String type;
    private Long parentId;

    public Transaction toTransaction(Long transactionId) {
        return new Transaction(transactionId, amount, type, parentId);
    }

}