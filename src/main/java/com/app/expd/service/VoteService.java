package com.app.expd.service;

import com.app.expd.dto.VoteDto;
import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.*;
import com.app.expd.repository.PostRepository;
import com.app.expd.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void save(VoteDto voteDto) {
        Post post = postRepository.findByPostID(voteDto.getPostId()).orElseThrow(
                ()-> new LetsTalkException("Post not found with ID : "+ voteDto.getPostId()));
        Optional<Vote> voteBypostandUser = voteRepository.findTopByPostAndUserOrderByVoteIDDesc(post, authService.getCurrentUser());

        if(voteBypostandUser.isPresent() && voteBypostandUser.get()
                .getVoteType().equals(voteDto.getVoteType())){
            throw new LetsTalkException("You have already "+ voteDto.getVoteType() + "'d for this post");
        }

        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            if(post.getVoteCount() == -1) {
                post.setVoteCount(1);
            } else  {
                post.setVoteCount(post.getVoteCount() + 1);
            }
        } else {
            if ( post.getVoteCount() == 1) {
                post.setVoteCount(-1);
            } else {
                post.setVoteCount(post.getVoteCount() - 1);
            }
        }

        voteRepository.save(mapVoteDtotoVote(voteDto, post));
        postRepository.save(post);
    }

    private Vote mapVoteDtotoVote(VoteDto voteDto, Post post) {

        return Vote.builder().post(post)
                .user(authService.getCurrentUser())
                .voteType(voteDto.getVoteType())
                .build();
    }

    public void deleteVote(Vote vote) {
        this.voteRepository.delete(vote);
    }
}
