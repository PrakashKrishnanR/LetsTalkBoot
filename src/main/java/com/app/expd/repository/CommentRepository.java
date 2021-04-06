package com.app.expd.repository;

import com.app.expd.models.Comment;
import com.app.expd.models.Post;
import com.app.expd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostOrderByCreatedDateDesc(Post post);
    List<Comment> findByUser(User user);
    List<Comment> findByPost(Post post);
    Optional<Comment> findByCommentID(Long commentID);
}
