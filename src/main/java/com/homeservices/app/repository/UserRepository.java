package com.homeservices.app.repository;

import com.homeservices.app.entity.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Long> {
    Optional<UserTable> findByEmail(String email);
    Optional<UserTable> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
