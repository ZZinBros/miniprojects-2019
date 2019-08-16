package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.exception.UserEditPageNotFoundException;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserEditPageController {

    private UserService userService;

    public UserEditPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable("id") Long id, UserSession userSession, Model model) {

        if (userSession.matchId(id)) {
            model.addAttribute("user", userService.findUserById(id));
            return "mypage-edit";
        }
        throw new UserEditPageNotFoundException("edit page not found");
    }
}
