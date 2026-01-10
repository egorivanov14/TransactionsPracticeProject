package org.example.repository;

import org.example.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
    "FROM Budget b WHERE b.account = :account " +
    "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    boolean existsCurrentByAccount(@Param("account") String account);


    @Query("SELECT b FROM Budget b WHERE b.account = :account " +
            "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findCurrentByAccount(@Param("account") String account);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Budget b WHERE b.account = :account " +
            "AND :date BETWEEN b.startDate AND b.endDate")
    boolean existsByAccountAndDate(@Param("account") String account,@Param("date") LocalDate date);


    @Query("SELECT b FROM Budget b WHERE b.account = :account " +
            "AND :date BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findByAccountAndDate(@Param("account") String account,@Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM Budget b WHERE b.account = :account " +
            "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    void deleteCurrentByAccount(@Param("account") String account);

    @Modifying
    @Query("DELETE FROM Budget b WHERE b.account = :account " +
            "AND :date BETWEEN b.startDate AND b.endDate")
    void deleteByAccountAndDate(@Param("account") String account, @Param("date") LocalDate date);



}
