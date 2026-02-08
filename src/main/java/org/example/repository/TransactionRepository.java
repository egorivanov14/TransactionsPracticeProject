package org.example.repository;

import org.example.dto.Type;
import org.example.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    @Query("SELECT t FROM Transaction t WHERE t.budget.account = :account " +
//            "AND t.user.email = :email")
//    List<Transaction> findAllByAccountAndUser(@Param("account") String account, @Param("email") String email);
//
//    @Query("SELECT t FROM Transaction t WHERE t.category = :category " +
//            "AND t.user.email = :email")
//    List<Transaction> findAllByCategoryAndUser(@Param("category") String category, @Param("email") String email);
//
//    @Query("SELECT t FROM Transaction t WHERE t.createdAt = :createdAt " +
//            "AND t.user.email = :email")
//    List<Transaction> findAllByCreatedAtAndUser(@Param("createdAt") LocalDate createdAt, @Param("email") String email);
//
//    @Query("SELECT t FROM Transaction t WHERE t.amount = :amount " +
//            "AND t.user.email = :email")
//    List<Transaction> findAllByAmountAndUser(@Param("amount") Long amount, @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.user.email = :email")
    Page<Transaction> findAllByBudgetIdAndUser(@Param("budgetId") Long budgetId, @Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.user.email = :email")
    Page<Transaction> findAllByUser(@Param("email") String email, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.type = :type AND t.user.email = :email")
    Long sumByType(@Param("budgetId") Long budgetId, @Param("type")Type type, @Param("email") String email);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.type = :type AND t.user.email = :email " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    Long sumByPeriodAndType(@Param("budgetId") Long budgetId,
                            @Param("type") Type type,
                            @Param("startDate") LocalDate startDate,
                            @Param("endDate") LocalDate endDate,
                            @Param("email") String email);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.type = :type AND t.user.email = :email " +
            "AND t.createdAt = :date")
    Long sumByTypeAndDay(@Param("budgetId") Long budgetId,
                            @Param("type") Type type,
                            @Param("date") LocalDate date,
                            @Param("email") String email);

    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.category = :category " +
            "AND t.user.email = :email")
    List<Transaction> findAllByBudgetIdAndCategoryAndUser(
            @Param("budgetId") Long budgetId, @Param("category") String category, @Param("email") String email);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.email = :email " +
            "AND t.type = 'INCOME' AND t.createdAt >= :startDate AND t.budget.id = :budgetId")
    Long getTotalIncome(@Param("email") String email,
                        @Param("startDate") LocalDate startDate,
                        @Param("budgetId") Long budgetId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.email = :email " +
            "AND t.type = 'EXPENDITURE' AND t.createdAt >= :startDate AND t.budget.id = :budgetId")
    Long getTotalExpenditure(@Param("email") String email,
                             @Param("startDate") LocalDate startDate,
                             @Param("budgetId") Long budgetId);

//    @Query("SELECT t FROM Transaction t WHERE t.user.email = :email AND t.createdAt >= :startDate " +
//            "AND t.budget.id = :budgetId " +
//            "ORDER BY t.createdAt ")
//    List<Transaction> getTransactionsByBudgetAndPeriod(@Param("email") String email,
//                                                       @Param("startDate") LocalDate startDate,
//                                                       @Param("budgetId") Long budgetId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.email = :email " +
            "AND t.createdAt >= :startDate AND t.budget.id = :budgetId")
    Long getTransactionsQuantity(@Param("email") String email,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("budgetId") Long budgetId);

    @Query("SELECT DISTINCT category FROM Transaction t WHERE t.user.email = :email AND t.createdAt >= :startDate " +
            "AND t.budget.id = :budgetId AND t.type = :type")
    List<String> getCategoriesByBudgetAndPeriod(@Param("email") String email,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("budgetId") Long budgetId,
                                                @Param("type") Type type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.budget.id = :budgetId " +
            "AND t.type = :type AND t.user.email = :email AND t.category = :category " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    Long sumByPeriodAndCategory(@Param("budgetId") Long budgetId,
                                @Param("type") Type type,
                                @Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate,
                                @Param("email") String email,
                                @Param("category") String category);
}