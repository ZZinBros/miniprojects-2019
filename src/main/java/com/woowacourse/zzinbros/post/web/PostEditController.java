package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostEditController {
    private PostService postService;

    public PostEditController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{id}/edit")
    public ModelAndView showEdit(@PathVariable long id, UserSession userSession) {
        ModelAndView modelAndView = new ModelAndView();
        Post post = postService.read(id);
        if (userSession.getId() == post.getAuthor().getId()) {
            modelAndView.setViewName("post-modify");
            modelAndView.addObject("post", postService.read(id));
            return modelAndView;
        }
        throw new UnAuthorizedException("게시글은 작성자만 수정할 수 있습니다.");
    }
}
