package com.app.expd.repository;

import com.app.expd.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationtokenRepository  extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
}
