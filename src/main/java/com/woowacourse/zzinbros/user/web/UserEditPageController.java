package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.exception.UserEditPageNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
        throw new UserEditPageNotFoundException();
    }
}
