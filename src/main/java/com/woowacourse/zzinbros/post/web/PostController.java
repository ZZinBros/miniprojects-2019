package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
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
    public ResponseEntity<Post> create(@RequestBody PostRequestDto dto, @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        Post post = postService.add(dto, loginUserDto.getId());
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> modify(@PathVariable long id, @RequestBody PostRequestDto dto, @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        Post post = postService.update(id, dto, loginUserDto.getId());
        return new ResponseEntity<>(post, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> remove(@PathVariable long id, @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        postService.delete(id, loginUserDto.getId());
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/like")
    public int updateLike(@PathVariable long id, @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        return postService.updateLike(id, loginUserDto.getId());
    }
}