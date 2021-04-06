package com.app.expd.service;

import com.app.expd.dto.PageablePost;
import com.app.expd.dto.PostRequest;
import com.app.expd.dto.PostResposne;
import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.*;
import com.app.expd.repository.*;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubTalkRepository subTalkRepository;
    private final AuthService authService;
    private final PostRepository postRepository;
    private final PostPageRepository postPageRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final VoteRepository voteRepository;

    public void save(PostRequest postRequest){
            SubTalk subTalk = subTalkRepository.findByCommName(postRequest.getSubtalkname()).
                    orElseThrow( () ->
                            new LetsTalkException("Not a Valid Subtalk community name : "+ postRequest.getSubtalkname()));
        User currentUser = authService.getCurrentUser();

        postRepository.save(mapToPostdomain(postRequest, subTalk, currentUser));
    }

    private Post mapToPostdomain(PostRequest postRequest, SubTalk subTalk, User currentUser) {
        return Post.builder().createdDate(Instant.now())
                .description(postRequest.getDescription())
                .subtalk(subTalk)
                .user(currentUser)
                .url(postRequest.getUrl())
                .voteCount(0)
                .postName(postRequest.getPostname())
                .build();
    }

    @Transactional(readOnly = true)
    public PostResposne findPostbyid(Long id){
       Post post = postRepository.findByPostID(id).orElseThrow(() ->
                new LetsTalkException(" Post with id : "+ id + " not found!!"));

       return postTopostResponse(post);

    }

    @Transactional(readOnly = true)
    public List<PostResposne> findallPosts(){
        return postRepository.findAllByOrderByCreatedDateDesc()
                .stream()
                .map(this::postTopostResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PageablePost findallPageablePost(Pageable pageable, String searchTerm){

        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdDate")));
        Page<Post> postPage = null;

        if(searchTerm.trim() != "" && searchTerm != null ){
            postPage = postPageRepository.findAllByDescriptionContainingIgnoreCaseOrPostNameContainingIgnoreCase(searchTerm, searchTerm, pageable1);
        } else {
            postPage = postPageRepository.findAll(pageable1);

        }
        List<PostResposne> postResponses = postPage
                .stream()
                .map(this::postTopostResponse)
                .collect(Collectors.toList());
        return PageablePost.builder()
                .postResposnes(postResponses)
                .totalElements(postPage.getNumberOfElements())
                .totalPages(postPage.getTotalPages())
                .build();
    }


    @Transactional(readOnly = true)
    public PageablePost getPostBySubtalkID(Long id, Pageable pageable, String searchTerm){

        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.desc("createdDate")));

        SubTalk subTalk = subTalkRepository.
                findById(id).orElseThrow(() ->new LetsTalkException(" No Subtalk found for the ID: "+ id));

        Page<Post> postPage = null;

        if(searchTerm.trim() != "" && searchTerm != null ){
            postPage = postPageRepository.findAllBySubtalkSubtalkIDAndDescriptionContainingIgnoreCaseOrPostNameContainingIgnoreCase(id, searchTerm, searchTerm, pageable1);
        } else {
            postPage = postPageRepository.findAllBySubtalkSubtalkID(id,pageable1);

        }

        List<PostResposne> postResposnes = postPage.stream()
                                            .map(this::postTopostResponse)
                                            .collect(Collectors.toList());
        return PageablePost.builder()
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getNumberOfElements())
                .postResposnes(postResposnes)
                .build();
    }

    @Transactional(readOnly = true)
    public PageablePost getAllpostByUsername(String username, Pageable pageable,
                                             String searchTerm){
        User user = userRepository.
                findByUsernameIgnoreCase(username).orElseThrow(() ->new LetsTalkException("The user name : "+ username + " is Invalid"));

        Page<Post> postPage = null;

        if(searchTerm.trim() != "" && searchTerm != null ){
            postPage = postPageRepository.findAllByUserUsernameAndDescriptionContainingIgnoreCaseOrPostNameContainingIgnoreCaseOrderByCreatedDate(username, searchTerm, searchTerm, pageable);
        } else {
            postPage = postPageRepository.findAllByUserUsernameOrderByCreatedDateDesc(username,pageable);

        }

        List<PostResposne> postResposnes = postPage
                .stream()
                .map(this::postTopostResponse)
                .collect(Collectors.toList());
        return PageablePost.builder()
                .postResposnes(postResposnes)
                .totalElements(postPage.getNumberOfElements())
                .totalPages(postPage.getTotalPages())
                .build();
    }

    private PostResposne postTopostResponse(Post post) {

        boolean createdbyUser = false;
        User user = this.authService.getCurrentUser();
        if(user.getRole().equals(Roles.ADMIN)){
            createdbyUser = true;
        } else {
            createdbyUser = user.equals(post.getUser());
        }


        return PostResposne.builder()
                .postId(post.getPostID())
                .description(post.getDescription())
                .postname(post.getPostName())
                .subtalkname(post.getSubtalk().getCommName())
                .url(post.getUrl())
                .createdByUser(createdbyUser)
                .voteCount(post.getVoteCount() == null ? 0 : post.getVoteCount())
                .commentCount(getCommentsCount(post))
                .duration(getDuration(post))
                .username(post.getUser().getUsername())
                .upVote(this.isUpVoted(post))
                .downVote(this.isDownVoted(post))
                .subtalkId(post.getSubtalk().getSubtalkID())
                .build();
    }

    public Boolean isDownVoted(Post post) {
        return this.checkVoteType(post, VoteType.DOWNVOTE);
    }

    public Boolean isUpVoted(Post post) {
        return this.checkVoteType(post, VoteType.UPVOTE);
    }

    public boolean checkVoteType(Post post, VoteType voteType){

        if( authService.isLoggedIn()){
            Optional<Vote> voteForPostByUser =
                    this.voteRepository.findTopByPostAndUserOrderByVoteIDDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }
    public Integer getCommentsCount(Post post){
        return commentRepository.findByPost(post).size();
    }

    public String getDuration(Post post){
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    public void deletePostByPostId(Long postid) {

        User user = authService.getCurrentUser();
        Post post = postRepository.findByPostID(postid).orElseThrow(() -> new LetsTalkException("No Post found for the given ID"));

        if( user.getRole().equals(Roles.USER)) {
            if (! user.equals(post.getUser())) {
                throw new LetsTalkException("Unauthorized action performed !!");
            }
        }
        //detach subtalk
        post.setSubtalk(null);
        //detach user
        post.setUser(null);
        deletePost(post);
    }

    public void deletePost(Post post) {
        this.postRepository.delete(post);
    }
}
