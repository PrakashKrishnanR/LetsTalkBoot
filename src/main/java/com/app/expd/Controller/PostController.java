package com.app.expd.Controller;

import com.app.expd.dto.PageablePost;
import com.app.expd.dto.PostRequest;
import com.app.expd.dto.PostResposne;
import com.app.expd.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@Slf4j
public class PostController {

    private PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){

        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResposne> getPostsbyID(@PathVariable Long id){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.findPostbyid(id));
    }

//    @GetMapping("")
//    private ResponseEntity<List<PostResposne>> getAllPosts(){
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(postService.findallPosts());
//    }

    @GetMapping("")
    private ResponseEntity<PageablePost> getAllPosts(Pageable pageable, @RequestParam("searchTerm") String searchTerm){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.findallPageablePost(pageable, searchTerm));
    }


    @GetMapping("/byTalkid/{id}")
    public ResponseEntity<PageablePost> getbyTalkid(@PathVariable Long id, Pageable pageable,
                                                    @RequestParam("searchTerm") String searchTerm){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPostBySubtalkID(id, pageable, searchTerm));

    }

    @GetMapping("/by-username")
    public ResponseEntity<PageablePost> getPostsbyUsername(@RequestParam String username,@RequestParam("searchTerm") String searchTerm,
                                                           Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllpostByUsername(username, pageable, searchTerm));
    }

    @DeleteMapping("/bypostid/{postid}")
    public ResponseEntity<Void> deletePostByPostID(@PathVariable Long postid) {
        postService.deletePostByPostId(postid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
