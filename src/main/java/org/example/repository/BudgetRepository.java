package org.example.repository;

import org.example.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

//    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
//    "FROM Budget b WHERE b.account = :account " +
//    "AND CURRENT_DATE BETWEEN b.startDate AND b.endDate")
//    boolean existsCurrentByAccount(@Param("account") String account);
//
//    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
//            "FROM Budget b WHERE b.account = :account " +
//            "AND :date BETWEEN b.startDate AND b.endDate")
//    boolean existsByAccountAndDate(@Param("account") String account,@Param("date") LocalDate date);

}
