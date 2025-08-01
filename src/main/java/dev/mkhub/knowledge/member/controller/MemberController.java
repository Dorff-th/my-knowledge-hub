package dev.mkhub.knowledge.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class MemberController {

    //thymeleaf에서 th:data-msg 속성으로 받기위해 파라미터를 받고 이를 다시 model로 붙임
    // 만약 이렇게 안하고 <body th:data-msg="${parmm.msg}"> 이렇게 하면 보안정책에 걸림
    @GetMapping("/login")
    public String loginForm(@RequestParam(name ="msg", required = false) String msg, Model model) {
        model.addAttribute("msg", msg);
        return "login";
    }

    @GetMapping("/")
    public String home(@RequestParam(name ="msg", required = false) String msg, Model model) {
        model.addAttribute("msg", msg);
        return "home";
    }


}
