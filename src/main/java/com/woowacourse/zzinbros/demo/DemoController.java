package com.woowacourse.zzinbros.demo;

import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

@Controller
public class DemoController {
    private PostService postService;
    private UserService userService;

    public DemoController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String demo(Model model, @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        Sort sort = by(Direction.DESC, "createdDateTime");
        model.addAttribute("posts", postService.readAll(loginUserDto.getId(), sort));
        model.addAttribute("users", userService.readAll());
        return "index";
    }

    @GetMapping("/entrance")
    public String enter() {
        return "entrance";
    }

    @RequestMapping(path = "/time", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String timeService() {
        return "\"" + ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"";
    }
}
