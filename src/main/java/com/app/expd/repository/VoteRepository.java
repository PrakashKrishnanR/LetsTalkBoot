package com.app.expd.repository;

import com.app.expd.models.Post;
import com.app.expd.models.User;
import com.app.expd.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIDDesc(Post post, User currenUser);

    Optional<Vote> findByVoteID(Long id);
}
