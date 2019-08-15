package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.stereotype.Controller;
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
    public ModelAndView editPage(@PathVariable("id") Long id, UserSession userSession) {
        ModelAndView modelAndView = new ModelAndView();

        if (userSession.matchId(id)) {
            modelAndView.addObject("user", userService.findUserById(id));
            modelAndView.setViewName("mypage-edit");
            return modelAndView;
        }
        modelAndView.addObject("errorMsg", "인가되지 않은 회원입니다");
        //        modelAndView.setViewName("index");
        return modelAndView;
    }
}
