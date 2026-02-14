package org.example.repository;

import org.example.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT r FROM RefreshToken r WHERE r.tokenHash = :hash AND r.revoked = false")
    Optional<RefreshToken> findValidByHash(@Param("hash") String hash);

    @Query("SELECT r FROM RefreshToken r WHERE r.userId = :userId AND r.revoked = false ")
    List<RefreshToken> findValidByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM RefreshToken r WHERE r.userId = :userId")
    List<RefreshToken> findAllByUser(@Param("userId") Long userId);

}