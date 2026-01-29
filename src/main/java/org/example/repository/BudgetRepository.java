package org.example.repository;

import org.example.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {


    @Query("SELECT b FROM Budget b JOIN FETCH b.user WHERE b.user.email = :email")
    List<Budget> findAllByUser(@Param("email") String email);

    @Query("SELECT b FROM Budget b WHERE b.id = :id AND b.user.email = :email")
    Optional<Budget> findByBudgetIdAndUser(@Param("id") Long id, @Param("email") String email);
}
