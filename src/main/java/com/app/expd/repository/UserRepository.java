package com.app.expd.repository;

import com.app.expd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByUsernameIgnoreCase(String userName);


}
