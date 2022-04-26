package com.manaraujo.mendel.model;

public record Transaction(Long transactionId, Double amount, String type, Long parentId) {}