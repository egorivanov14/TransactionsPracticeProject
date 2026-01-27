package org.example.repository;

import org.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.budget.account = :account")
    List<Transaction> findAllByAccount(@Param("account") String account);

    List<Transaction> findAllByCategory(String category);

    List<Transaction> findAllByCreatedAt(LocalDate createdAt);

    List<Transaction> findAllByAmount(Long amount);

    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId")
    List<Transaction> findAllByBudgetId(@Param("budgetId") Long budgetId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId")
    Long sumAmountByBudgetId(@Param("budgetId") Long budgetId);

    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.category = :category ")
    List<Transaction> findAllByBudgetIdAndCategory(
            @Param("budgetId") Long budgetId, @Param("category") String category);
}