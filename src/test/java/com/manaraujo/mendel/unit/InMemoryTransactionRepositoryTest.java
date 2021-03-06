package com.manaraujo.mendel.unit;

import com.manaraujo.mendel.exception.NotFoundException;
import com.manaraujo.mendel.model.Transaction;
import com.manaraujo.mendel.repository.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static com.manaraujo.mendel.model.Transaction.buildTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class InMemoryTransactionRepositoryTest {

    @TestConfiguration
    static class testContextConfiguration {

        @Bean
        public InMemoryTransactionRepository inMemoryTransactionRepository() {
            return new InMemoryTransactionRepository();
        }

    }

    @Autowired
    private InMemoryTransactionRepository inMemoryTransactionRepository;

    @BeforeEach
    void init() {
        inMemoryTransactionRepository = new InMemoryTransactionRepository();
    }

    @Test
    public void save() {
        Transaction transaction = buildTransaction(1L, 100.0, "test", null);
        inMemoryTransactionRepository.save(transaction);
        Transaction saved = inMemoryTransactionRepository.getById(transaction.getTransactionId());
        assertEquals(transaction, saved);
    }

    @Test
    public void save_parentNotFound() {
        Transaction transaction = buildTransaction(1L, 100.0, "test", 2L);
        assertThatThrownBy(() -> inMemoryTransactionRepository.save(transaction))
                .isInstanceOf(NotFoundException.class)
                .hasFieldOrPropertyWithValue("code", "not_found")
                .hasFieldOrPropertyWithValue("description", "Parent_id 2 not found");
    }

    @Test
    public void getById() {
        Transaction transaction = buildTransaction(1L, 100.0, "test", null);
        inMemoryTransactionRepository.save(transaction);
        Transaction result = inMemoryTransactionRepository.getById(1L);
        assertEquals(transaction, result);
    }

    @Test
    public void getById_notFound() {
        assertThatThrownBy(() -> inMemoryTransactionRepository.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasFieldOrPropertyWithValue("code", "not_found")
                .hasFieldOrPropertyWithValue("description", "Transaction with id 1 not found");
    }

    @Test
    public void getChildren() {
        Transaction transaction1 = buildTransaction(1L, 100.0, "test_1", null);
        Transaction transaction2 = buildTransaction(2L, 100.0, "test_1", 1L);
        inMemoryTransactionRepository.save(transaction1);
        inMemoryTransactionRepository.save(transaction2);

        Set<Transaction> result = inMemoryTransactionRepository.getChildren(1L);

        assertEquals(Set.of(transaction2), result);
    }

    @Test
    public void getChildren_empty() {
        Transaction transaction1 = buildTransaction(1L, 100.0, "test_1", null);
        inMemoryTransactionRepository.save(transaction1);

        Set<Transaction> result = inMemoryTransactionRepository.getChildren(2L);

        assertThat(result).isEmpty();
    }

    @Test
    public void getByType_empty() {
        assertThatThrownBy(() -> inMemoryTransactionRepository.getIdsByType("test"))
                .isInstanceOf(NotFoundException.class)
                .hasFieldOrPropertyWithValue("code", "not_found")
                .hasFieldOrPropertyWithValue("description", "Type test not found");
    }

    @Test
    public void getByType() {
        Transaction transaction1 = buildTransaction(1L, 100.0, "test_1", null);
        Transaction transaction2 = buildTransaction(2L, 100.0, "test_1", null);
        Transaction transaction3 = buildTransaction(3L, 100.0, "test_2", null);
        inMemoryTransactionRepository.save(transaction1);
        inMemoryTransactionRepository.save(transaction2);
        inMemoryTransactionRepository.save(transaction3);

        Set<Long> result = inMemoryTransactionRepository.getIdsByType("test_1");

        assertEquals(Set.of(1L, 2L), result);
    }

}
