package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);

    User findByEmail(String email);
}
