package com.khun.balance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {

    @GetMapping("/signin")
    public void loadSignIn(){
    }
    @PostMapping("/signin")
    public String signIn(){
        return "redirect:/";
    }
    @GetMapping("/signup")
    public void loadSignUp(){
    }
    @PostMapping("/signup")
    public String signUp(){
        return "redirect:/";
    }
    @GetMapping("/signout")
    public String signOut(){
        return "redirect:/signin";
    }
    @PostMapping("user/changePass")
    public String changePass(){
        return "redirect:/";
    }
}
