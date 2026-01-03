package org.example.repository;

import org.example.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByCategory(String category);

    boolean existsByCategory(String category);

    void deleteByCategory(String category);
}
