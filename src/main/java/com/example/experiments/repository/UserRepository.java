package com.example.experiments.repository;

import com.example.experiments.model.Account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Annotation not needed on interfaces that extend JpaRepository
// Repository - class interacts with datasource
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // NOTE: repository is
    //      where data is stored and
    //      provides CRUD operations on objects

    // NOTE: Spring generated query cause it follows naming standard findBy___
    Optional<User> findByEmail(String email);
}
