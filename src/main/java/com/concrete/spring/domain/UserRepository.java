package com.concrete.spring.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {
    @Query(value = "SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query(value = "SELECT u FROM User u WHERE u.email = :email AND password = :password")
    User findByEmailAndPassword(@Param("email") String email,
                                @Param("password") String password);

    @Query(value = "SELECT u FROM User u WHERE u.token = :token")
    User findByToken(@Param("token") String token);
}
