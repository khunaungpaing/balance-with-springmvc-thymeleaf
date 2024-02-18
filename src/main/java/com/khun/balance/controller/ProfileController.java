package com.khun.balance.controller;

import com.khun.balance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("user/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    @GetMapping
    String index(ModelMap model){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var userVo = userService.findByLoginId(username);
        model.put("user",userVo);
        return "profile";
    }

    @PostMapping("/contact")
    String updateContact(@RequestParam(required = false) String phone, @RequestParam(required = false) String email){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateContact(username,phone,email);
        return "redirect:/user/profile ";
    }
}
