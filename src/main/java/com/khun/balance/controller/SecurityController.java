package com.khun.balance.controller;

import com.khun.balance.domain.form.SignUpForm;
import com.khun.balance.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

    private final UserService userService;

    @GetMapping("/signin")
    public String loadSignIn(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "signin";
        }
        return "redirect:/";
    }

    @GetMapping("/signup")
    public void loadSignUp(){
    }
    @PostMapping("/signup")
    public String signUp(@ModelAttribute("form") @Validated SignUpForm form, BindingResult result){
        if (result.hasErrors()){
            log.error("{}",result.getAllErrors());
            return "signup";
        }
        log.info("form : {}",form);
        userService.signUp(form);
        return "redirect:/";
    }

    @PostMapping("user/changePass")
    public String changePass(){
        return "redirect:/";
    }

    @ModelAttribute(name = "form")
    SignUpForm form(){
        return new SignUpForm();
    }
}
