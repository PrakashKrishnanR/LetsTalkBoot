package com.app.expd.Controller;


import com.app.expd.dto.CommentsDto;
import com.app.expd.models.Comment;
import com.app.expd.service.AuthService;
import com.app.expd.service.CommentsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
@Slf4j
public class CommentsController {

    private final CommentsService commentsService;
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto comments){
        commentsService.saveComment(comments);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/byPost/{id}")
    public ResponseEntity<List<CommentsDto>> getAllcommentsBypostid(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
        .body(commentsService.getAllcommentsByPostid(id));
    }

    @GetMapping("/byUsername/{username}")
    public ResponseEntity<List<CommentsDto>> getAllcommentsByUsername(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK)
        .body(commentsService.getAllCommentsByUsername(username));
    }

    @DeleteMapping("/deleteById/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){

        commentsService.deleteComments(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
