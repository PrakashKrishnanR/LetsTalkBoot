package com.app.expd.repository;

import com.app.expd.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostPageRepository extends PagingAndSortingRepository<Post, Long> {

    @Override
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByDescriptionContainingIgnoreCaseOrPostNameContainingIgnoreCase(String searchTerm, String term, Pageable page );

    Page<Post> findAllBySubtalkSubtalkID(Long id, Pageable pageable);

    Page<Post> findAllBySubtalkSubtalkIDAndDescriptionContainingIgnoreCaseOrPostNameContainingIgnoreCase(Long Id, String searchTerm, String term, Pageable pageable);

    Page<Post> findAllByUserUsernameOrderByCreatedDateDesc(String username, Pageable pageable);

    Page<Post> findAllByUserUsernameAndDescriptionContainingIgnoreCaseOrPostNameContainingIgnoreCaseOrderByCreatedDate(String username, String searchTerm, String term, Pageable pageable);
}
