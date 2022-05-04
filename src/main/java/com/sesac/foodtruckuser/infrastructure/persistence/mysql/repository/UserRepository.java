package com.sesac.foodtruckuser.infrastructure.persistence.mysql.repository;

import com.sesac.foodtruckuser.infrastructure.persistence.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    int countByUsername(String username);

    int countByEmail(String email);
}
