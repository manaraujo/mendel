package com.manaraujo.mendel.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.manaraujo.mendel.model.Transaction;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

import static com.manaraujo.mendel.model.Transaction.buildTransaction;

@Getter
@Setter
@Data
@Builder
public class TransactionDTO {

    @NotNull(message = "Amount is required")
    private Double amount;
    @NotNull(message = "Type is required")
    private String type;
    private Long parentId;

    public Transaction toTransaction(Long transactionId) {
        return buildTransaction(transactionId, amount, type, parentId);
    }

}