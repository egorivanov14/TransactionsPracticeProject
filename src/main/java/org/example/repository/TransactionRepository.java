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

    @Query("SELECT t FROM Transaction t WHERE t.budget.account = :account " +
            "AND t.user.email = :email")
    List<Transaction> findAllByAccountAndUser(@Param("account") String account, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.category = :category " +
            "AND t.user.email = :email")
    List<Transaction> findAllByCategoryAndUser(@Param("category") String category, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt = :createdAt " +
            "AND t.user.email = :email")
    List<Transaction> findAllByCreatedAtAndUser(@Param("createdAt") LocalDate createdAt, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.amount = :amount " +
            "AND t.user.email = :email")
    List<Transaction> findAllByAmountAndUser(@Param("amount") Long amount, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.user.email = :email")
    List<Transaction> findAllByBudgetIdAndUser(@Param("budgetId") Long budgetId, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.user.email = :email")
    List<Transaction> findAllByUser(@Param("email") String email);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.user.email = :email")
    Long sumAmountByBudgetId(@Param("budgetId") Long budgetId, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.category = :category " +
            "AND t.user.email = :email")
    List<Transaction> findAllByBudgetIdAndCategoryAndUser(
            @Param("budgetId") Long budgetId, @Param("category") String category, @Param("email") String email);
}