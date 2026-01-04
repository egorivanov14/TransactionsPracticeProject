package org.example.repository;

import org.example.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
    "FROM Budget b WHERE b.category = :category " +
    "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    boolean existsCurrentByCategory(@Param("category") String category);


    @Query("SELECT b FROM Budget b WHERE b.category = :category" +
            " AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findCurrentByCategory(@Param("category") String category);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Budget b WHERE b.category = :category " +
            "AND :date BETWEEN b.startDate AND b.endDate")
    boolean existsByCategoryAndDate(@Param("category") String category,@Param("date") LocalDate date);


    @Query("SELECT b FROM Budget b WHERE b.category = :category " +
            "AND :date BETWEEN b.startDate AND b.endDate")
    Optional<Budget> findByCategoryAndDate(@Param("category") String category,@Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM Budget b WHERE b.category = :category " +
            "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
    void deleteCurrentByCategory(@Param("category") String category);

    @Modifying
    @Query("DELETE FROM Budget b WHERE b.category = :category " +
            "AND :date BETWEEN b.startDate AND b.endDate")
    void deleteByCategoryAndDate(@Param("category") String category,@Param("date") LocalDate date);



}
