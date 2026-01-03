package org.example.repository;

import org.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCategory(String category);

    List<Transaction> findAllByCreatedAt(LocalDate createdAt);

    List<Transaction> findAllByAmount(Long amount);

}
