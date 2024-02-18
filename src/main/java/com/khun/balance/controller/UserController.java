package com.khun.balance.controller;

import com.khun.balance.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping
    public String search(
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            ModelMap model
    ){
        var list = userService.search(status,name,phone);
        model.put("userList",list);
        return "users";
    }

    @PostMapping("status")
    String changeStatus(
            @RequestParam Long id,
            @RequestParam boolean status
    ){
        userService.changeStatus(id,!status);
        return "redirect:/admin/users";
    }
}
