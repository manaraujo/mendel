package com.manaraujo.mendel.dto;

public record TransactionDTO (Long transactionId, Double amount, String type, Long parentId) {}