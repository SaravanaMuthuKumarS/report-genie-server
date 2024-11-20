package com.i2i.rgs.repository;


import java.util.List;

import com.i2i.rgs.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for performing CRUD operations on the Employee entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByIsDeletedFalse(Pageable pageable);
    User findByIdAndIsDeletedFalse(String id);
    User findByEmail(String username);

    boolean existsByEmailAndIsDeletedFalse(@Email String email);

    boolean existsByEmail(@Email(message = "Email should be in valid format(Eg. user@gmail.com)") String email);
}


