package com.app.expd.service;

import com.app.expd.constants.MessageConstants;
import com.app.expd.dto.CommentsDto;
import com.app.expd.exceptions.LetsTalkException;
import com.app.expd.models.*;
import com.app.expd.repository.CommentRepository;
import com.app.expd.repository.PostRepository;
import com.app.expd.repository.UserRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentsService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final MessageConstants messageConstants;

    public void saveComment(CommentsDto comments){

       Post post = postRepository.findByPostID(comments.getPostId()).orElseThrow(() ->
                new LetsTalkException("Not a valid Post Id : "+ comments.getPostId()));

       Comment comment = mapdtoToCommentsDomain(comments, post, authService.getCurrentUser());
       commentRepository.save(comment);
       User user = this.authService.getCurrentUser();
       //TODO notify Comments to post owner using Mail-service
        mailService.sendCommentNotification(new NotificationEmail(user.getName() + messageConstants.getCommentNotification(),
                post.getUser().getUsername(), messageConstants.getCommentLink()+messageConstants.getFrontEndPostUrl()+post.getPostID()));
    }

    private Comment mapdtoToCommentsDomain(CommentsDto comments, Post post, User user) {

        return Comment.builder()
                .createdDate(Instant.now())
                .post(post)
                .text(comments.getText())
                .user(user)
                .build();
    }

    public List<CommentsDto> getAllcommentsByPostid(Long id) {

        User user = this.authService.getCurrentUser();
        Post post = postRepository.findByPostID(id)
                .orElseThrow(() -> new LetsTalkException("Not a valid postid "+ id.toString() ));


        List<Comment> comments =  commentRepository.findByPostOrderByCreatedDateDesc(post);

        List<CommentsDto> commentsDtoList = new ArrayList<>();

        for(Comment comment: comments) {
            commentsDtoList.add(this.mapCommentsTODto(comment,user));
        }

        return commentsDtoList;
    }

    private CommentsDto mapCommentsTODto(Comment comment) {

        return CommentsDto.builder()
                .postId(comment.getPost().getPostID())
                .text(comment.getText())
                .username(comment.getUser().getUsername())
                .id(comment.getCommentID())
                .duration(getDuration(comment.getCreatedDate()))
                .build();
    }

    private CommentsDto mapCommentsTODto(Comment comment, User user) {

        boolean delete = false;

        if(user.getRole().equals(Roles.ADMIN)){
            delete = true;
        } else if (user.equals(comment.getUser())) {
            delete = true;
        }

        return CommentsDto.builder()
                .postId(comment.getPost().getPostID())
                .text(comment.getText())
                .canDeleteComment(delete)
                .username(comment.getUser().getUsername())
                .id(comment.getCommentID())
                .duration(getDuration(comment.getCreatedDate()))
                .build();
    }

    public List<CommentsDto> getAllCommentsByUsername(String username) {

        User user = userRepository.findByUsername(username).
                orElseThrow(()-> new LetsTalkException("Not a Vlaid username : "+username));
        return commentRepository.findByUser(user)
                .stream()
                .map(this::mapCommentsTODto)
                .collect(Collectors.toList());
    }

    public void deleteComments(Long id) {
        User user = authService.getCurrentUser();
        Comment comment = commentRepository.findByCommentID(id).orElseThrow(() -> new LetsTalkException("No Comment found for the given ID"));

        if( user.getRole().equals(Roles.USER)) {
            if (!user.equals(comment.getUser())) {
                throw new LetsTalkException("Unauthorized action performed !!");
            }
        }
        commentRepository.delete(comment);
    }

    public void deleteComments(Comment comment) {
        commentRepository.delete(comment);
    }

    public String getDuration(Instant createdDate){
        return TimeAgo.using(createdDate.toEpochMilli());
    }
}
