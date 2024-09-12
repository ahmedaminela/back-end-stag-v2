package com.example.rh.repository;

import com.example.rh.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u JOIN u.authorities r WHERE r.authority = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);
    // Add this method if not already present

}
