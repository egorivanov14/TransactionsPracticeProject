package org.example.repository;

import org.example.entity.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @EntityGraph(attributePaths = "user")
    @Query("SELECT b FROM Budget b WHERE b.user.email = :email")
    Page<Budget> findAllByUser(@Param("email") String email, Pageable pageable);

    @Query("SELECT b FROM Budget b WHERE b.id = :id AND b.user.email = :email")
    Optional<Budget> findByBudgetIdAndUser(@Param("id") Long id, @Param("email") String email);

    boolean existsByIdAndUserEmail(Long id, String email);
}
