package com.khun.balance.controller;

import com.khun.balance.service.UserAccessLogService;
import com.khun.balance.service.UserService;
import com.khun.balance.utils.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
@RequestMapping("user/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;
    private final UserAccessLogService userAccessLogService;

    @GetMapping
    String index(
            ModelMap model,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size
    ) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var userVo = userService.findByLoginId(username);
        var accessLogs = userAccessLogService.search(username, page, size);
        var pagination = Pagination.builder("/user/profile").page(accessLogs).build();
        model.put("user", userVo);
        model.put("accessLogs", accessLogs.getContent());
        model.put("pagination", pagination);
        return "profile";
    }

    @PostMapping("/contact")
    String updateContact(@RequestParam(required = false) String phone, @RequestParam(required = false) String email) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateContact(username, phone, email);
        return "redirect:/user/profile ";
    }
}
