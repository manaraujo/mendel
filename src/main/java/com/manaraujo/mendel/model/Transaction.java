package com.manaraujo.mendel.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class Transaction {

    private Long transactionId;
    private Double amount;
    private String type;
    private Long parentId;

    public static Transaction buildTransaction(Long transactionId, Double amount, String type, Long parentId) {
        return builder()
                .transactionId(transactionId)
                .amount(amount)
                .type(type)
                .parentId(parentId)
                .build();
    }
}