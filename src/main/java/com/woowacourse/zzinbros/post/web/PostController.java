package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Post show(@PathVariable long id) {
        return postService.read(id);
    }

    @PostMapping
    public Post create(@RequestBody PostRequestDto dto, UserSession userSession) {
        return postService.add(dto, userSession);
    }

    @PutMapping("/{id}")
    public Post modify(@PathVariable long id, @RequestBody PostRequestDto dto, UserSession userSession) {
        return postService.update(id, dto, userSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> remove(@PathVariable long id, UserSession userSession) {
        postService.delete(id, userSession);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}