package com.app.expd.repository;

import com.app.expd.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostID(Long id);

    List<Post> findByUserUsernameOrderByCreatedDateDesc(String username);

    List<Post> findAllByOrderByCreatedDateDesc();

}
