package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> show(@PathVariable long id) {
        Post retrievePost = postService.read(id);
        return new ResponseEntity<>(retrievePost, HttpStatus.OK);
    }

    @PostMapping
    public Post create(@RequestBody PostRequestDto dto, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 임시
        loggedInUser = userService.findUserById(999L);

        return postService.add(dto, loggedInUser);
    }

    @PutMapping("/{id}")
    public Post modify(@PathVariable long id, @RequestBody PostRequestDto dto, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 임시
        loggedInUser = userService.findUserById(999L);

        return postService.update(id, dto, loggedInUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> remove(@PathVariable long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // 임시
        loggedInUser = userService.findUserById(999L);

        postService.delete(id, loggedInUser);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}