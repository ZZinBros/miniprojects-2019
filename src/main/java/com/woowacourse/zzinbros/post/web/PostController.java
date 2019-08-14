package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable long id) {
        Post retrievePost = postService.read(id);
        return new ResponseEntity<>(retrievePost, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Post> create(PostRequestDto dto, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Post persistPost = postService.add(dto, loggedInUser);
        return new ResponseEntity<>(persistPost, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> modify(@PathVariable long id, PostRequestDto dto, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Post updatePost = postService.update(id, dto, loggedInUser);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> remove(@PathVariable long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        postService.delete(id, loggedInUser);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}